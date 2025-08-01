const Order = require('../models/checkout/Order');
const orderBroadcastService = require('../services/orderBroadcastService');

// Handle vendor notifications
const handleVendorNotification = async (req, res) => {
    try {
        console.log('📥 Vendor notification received:', req.body);
        
        const { userId: orderId, status, vendorId, vendorName, message } = req.body;

        // Validate required fields
        if (!orderId || !status) {
            return res.status(400).json({
                success: false,
                message: 'Missing required fields: orderId and status are required'
            });
        }

        // Find and update the order
        const order = await Order.findById(orderId);
        if (!order) {
            return res.status(404).json({ 
                success: false, 
                message: 'Order not found' 
            });
        }

        // Update order status based on vendor notification
        const statusMapping = {
            'accepted': 'VENDOR_ASSIGNED',
            'preparingOrder': 'PREPARING',
            'readyForPickup': 'READY',
            'completed': 'COMPLETED',
            'skipped': 'PENDING_VENDOR_SELECTION',
            'expired': 'CANCELLED'
        };

        const newStatus = statusMapping[status] || status.toUpperCase();
        const previousStatus = order.status;

        // Update order status
        order.status = newStatus;
        
        // If vendor accepted, assign vendor details
        if (status === 'accepted') {
            order.assignedVendor = {
                vendorId,
                vendorName,
                assignedAt: new Date()
            };
        }

        // Add vendor message if provided
        if (message) {
            order.vendorMessages = order.vendorMessages || [];
            order.vendorMessages.push({
                message,
                type: 'VENDOR_MESSAGE',
                sender: 'vendor',
                timestamp: new Date()
            });
        }

        await order.save();

        // Handle different notification types through broadcast service
        if (status === 'accepted') {
            orderBroadcastService.handleVendorAcceptance(req.body);
        } else {
            orderBroadcastService.handleStatusUpdate(req.body);
        }

        console.log(`✅ Order ${orderId} status updated: ${previousStatus} → ${newStatus}`);

        res.status(200).json({
            success: true,
            message: 'Notification processed successfully',
            orderId,
            previousStatus,
            newStatus,
            vendorInfo: status === 'accepted' ? {
                vendorId,
                vendorName,
                assignedAt: new Date()
            } : undefined
        });

    } catch (error) {
        console.error('❌ Error handling vendor notification:', error);
        res.status(500).json({
            success: false,
            message: 'Failed to process vendor notification',
            error: error.message
        });
    }
};

// Get vendor notification status (optional utility)
const getNotificationStatus = async (req, res) => {
    try {
        const { orderId } = req.params;

        const order = await Order.findById(orderId)
            .select('status assignedVendor vendorMessages')
            .lean();

        if (!order) {
            return res.status(404).json({
                success: false,
                message: 'Order not found'
            });
        }

        res.status(200).json({
            success: true,
            order: {
                orderId,
                status: order.status,
                assignedVendor: order.assignedVendor,
                messageCount: order.vendorMessages?.length || 0,
                lastMessage: order.vendorMessages?.slice(-1)[0] || null
            }
        });

    } catch (error) {
        console.error('❌ Error getting notification status:', error);
        res.status(500).json({
            success: false,
            message: 'Failed to get notification status',
            error: error.message
        });
    }
};

module.exports = {
    handleVendorNotification,
    getNotificationStatus
};