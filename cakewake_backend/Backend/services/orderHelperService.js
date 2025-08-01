const Order = require('../models/checkout/Order');

class OrderHelperService {
  
  // Initialize database indexes
  static async initializeIndexes() {
    try {
      // Create indexes for better query performance
      await Order.collection.createIndex({ customer: 1, createdAt: -1 });
      await Order.collection.createIndex({ 'selectedVendor.vendorId': 1, status: 1 });
      await Order.collection.createIndex({ status: 1, createdAt: -1 });
      await Order.collection.createIndex({ city: 1, area: 1 });
      await Order.collection.createIndex({ 'broadcastDetails.broadcastId': 1 });
      await Order.collection.createIndex({ 'dedicatedVendorConnection.vendorId': 1 });
      
      console.log('✅ Order indexes created successfully');
    } catch (error) {
      console.error('❌ Error creating order indexes:', error);
    }
  }

  // Generate order reference number
  static generateOrderReference(order) {
    const date = order.createdAt || new Date();
    return `CW${date.getFullYear()}${String(date.getMonth() + 1).padStart(2, '0')}${String(date.getDate()).padStart(2, '0')}${order._id.toString().slice(-6).toUpperCase()}`;
  }

  // Update order status with timestamp and history
  static async updateOrderStatus(orderId, newStatus, updatedBy = 'SYSTEM', message = '') {
    try {
      const order = await Order.findById(orderId);
      if (!order) {
        throw new Error('Order not found');
      }

      const now = new Date();
      
      // Update main status
      order.status = newStatus;
      
      // Update specific timestamp based on status
      switch (newStatus) {
        case 'VENDOR_ASSIGNED':
          order.timestamps.vendorSelected = now;
          break;
        case 'PAYMENT_COMPLETED':
          order.timestamps.paymentCompleted = now;
          break;
        case 'CONFIRMED':
          order.timestamps.orderConfirmed = now;
          break;
        case 'PREPARING':
          order.timestamps.preparationStarted = now;
          break;
        case 'READY':
          order.timestamps.orderReady = now;
          break;
        case 'OUT_FOR_DELIVERY':
          order.timestamps.outForDelivery = now;
          break;
        case 'DELIVERED':
          order.timestamps.delivered = now;
          break;
        case 'COMPLETED':
          order.timestamps.completed = now;
          break;
        case 'CANCELLED':
          order.timestamps.cancelled = now;
          break;
      }
      
      // Add to status history
      order.statusHistory.push({
        status: newStatus,
        message: message,
        timestamp: now,
        updatedBy: updatedBy
      });

      await order.save();
      return order;
    } catch (error) {
      console.error('Error updating order status:', error);
      throw error;
    }
  }

  // Get current order stage (1-12)
  static getCurrentStage(status) {
    const stages = [
      'DRAFT',
      'PENDING_VENDOR_SELECTION',
      'BROADCASTING_TO_VENDORS',
      'VENDOR_ASSIGNED',
      'PAYMENT_PENDING',
      'PAYMENT_COMPLETED',
      'CONFIRMED',
      'PREPARING',
      'READY',
      'OUT_FOR_DELIVERY',
      'DELIVERED',
      'COMPLETED'
    ];
    
    return stages.indexOf(status) + 1;
  }

  // Check if order can be cancelled
  static canBeCancelled(status) {
    const nonCancellableStates = ['DELIVERED', 'COMPLETED', 'CANCELLED'];
    return !nonCancellableStates.includes(status);
  }

  // Get estimated delivery time
  static getEstimatedDeliveryTime(order) {
    if (order.selectedVendor && order.selectedVendor.estimatedCompletionTime) {
      return order.selectedVendor.estimatedCompletionTime;
    }
    
    // Default estimation: 2 hours from order confirmation
    const estimatedTime = new Date(order.timestamps.orderConfirmed || order.createdAt);
    estimatedTime.setHours(estimatedTime.getHours() + 2);
    return estimatedTime;
  }

  // Add message to order
  static async addMessage(orderId, sender, message, messageType = 'TEXT') {
    try {
      const order = await Order.findById(orderId);
      if (!order) {
        throw new Error('Order not found');
      }

      order.messages.push({
        sender,
        message,
        messageType,
        timestamp: new Date()
      });

      await order.save();
      return order;
    } catch (error) {
      console.error('Error adding message to order:', error);
      throw error;
    }
  }

  // Update vendor connection status
  static async updateVendorConnectionStatus(orderId, status, vendorId) {
    try {
      const order = await Order.findById(orderId);
      if (!order) {
        throw new Error('Order not found');
      }

      if (!order.dedicatedVendorConnection) {
        order.dedicatedVendorConnection = {};
      }
      
      order.dedicatedVendorConnection.connectionStatus = status;
      order.dedicatedVendorConnection.lastActiveAt = new Date();
      
      if (vendorId) {
        order.dedicatedVendorConnection.vendorId = vendorId;
      }
      
      if (status === 'CONNECTED' && !order.dedicatedVendorConnection.establishedAt) {
        order.dedicatedVendorConnection.establishedAt = new Date();
      }
      
      await order.save();
      return order;
    } catch (error) {
      console.error('Error updating vendor connection status:', error);
      throw error;
    }
  }

  // Find orders by vendor
  static async findByVendor(vendorId, status = null) {
    try {
      const query = { 'selectedVendor.vendorId': vendorId };
      if (status) {
        query.status = status;
      }
      return await Order.find(query).sort({ createdAt: -1 });
    } catch (error) {
      console.error('Error finding orders by vendor:', error);
      throw error;
    }
  }

  // Find orders requiring vendor selection
  static async findPendingVendorSelection(city, area) {
    try {
      return await Order.find({
        status: 'PENDING_VENDOR_SELECTION',
        city,
        area
      }).sort({ createdAt: 1 });
    } catch (error) {
      console.error('Error finding pending vendor selection orders:', error);
      throw error;
    }
  }

  // Find broadcasting orders
  static async findBroadcastingOrders() {
    try {
      return await Order.find({
        status: 'BROADCASTING_TO_VENDORS',
        'broadcastDetails.expiresAt': { $gt: new Date() }
      }).sort({ 'broadcastDetails.broadcastTime': 1 });
    } catch (error) {
      console.error('Error finding broadcasting orders:', error);
      throw error;
    }
  }

  // Get order statistics
  static async getOrderStatistics(customerId = null) {
    try {
      const matchQuery = customerId ? { customer: customerId } : {};
      
      const stats = await Order.aggregate([
        { $match: matchQuery },
        {
          $group: {
            _id: '$status',
            count: { $sum: 1 },
            totalAmount: { $sum: '$amount' }
          }
        }
      ]);

      return stats.reduce((acc, stat) => {
        acc[stat._id] = {
          count: stat.count,
          totalAmount: stat.totalAmount
        };
        return acc;
      }, {});
    } catch (error) {
      console.error('Error getting order statistics:', error);
      throw error;
    }
  }

  // Get orders with vendor connection status
  static async getOrdersWithVendorStatus(customerId) {
    try {
      const orders = await Order.find({ customer: customerId })
        .sort({ createdAt: -1 });

      return orders.map(order => ({
        ...order.toObject(),
        orderReference: this.generateOrderReference(order),
        currentStage: this.getCurrentStage(order.status),
        canBeCancelled: this.canBeCancelled(order.status),
        estimatedDeliveryTime: this.getEstimatedDeliveryTime(order)
      }));
    } catch (error) {
      console.error('Error getting orders with vendor status:', error);
      throw error;
    }
  }

  // Cleanup expired broadcasts
  static async cleanupExpiredBroadcasts() {
    try {
      const expiredOrders = await Order.find({
        status: 'BROADCASTING_TO_VENDORS',
        'broadcastDetails.expiresAt': { $lt: new Date() }
      });

      for (const order of expiredOrders) {
        await this.updateOrderStatus(
          order._id,
          'CANCELLED',
          'SYSTEM',
          'Order cancelled due to no vendor acceptance within timeout period'
        );
      }

      console.log(`✅ Cleaned up ${expiredOrders.length} expired broadcast orders`);
      return expiredOrders.length;
    } catch (error) {
      console.error('Error cleaning up expired broadcasts:', error);
      throw error;
    }
  }
}

module.exports = OrderHelperService;