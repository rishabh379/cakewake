const { EventEmitter } = require('events');

class OrderBroadcastService extends EventEmitter {
    constructor() {
        super();
        this.pendingOrders = new Map();
        this.BROADCAST_TIMEOUT = 300000; // 5 minutes
        
        console.log('🔧 OrderBroadcastService initialized for Socket.IO');
    }

    // Broadcast order to vendors via Socket.IO
    async broadcastOrder(orderId, orderDetails, availableVendors) {
        try {
            console.log('📡 Broadcasting order via Socket.IO');
            console.log('🎯 Target vendors:', availableVendors.map(v => v.vendorId));

            const workArea = `${orderDetails.deliveryLocation.city}-${orderDetails.deliveryLocation.area}`;
            
            const broadcastData = {
                orderId: orderId,
                orderDetails: {
                    cakeName: orderDetails.items[0]?.name || 'Custom Cake',
                    amount: orderDetails.totalAmount,
                    quantity: orderDetails.items[0]?.quantity || 1,
                    cakeWeight: orderDetails.items[0]?.weight || '1kg',
                    customerName: orderDetails.customerInfo?.name || 'Customer',
                    elements: orderDetails.items[0]?.elements || [],
                    orderDate: orderDetails.orderDate,
                    orderTime: orderDetails.orderTime,
                    deliveryLocation: orderDetails.deliveryLocation
                },
                workArea: workArea,
                availableVendors: availableVendors.map(v => v.vendorId),
                expiresAt: new Date(Date.now() + this.BROADCAST_TIMEOUT),
                timestamp: new Date()
            };

            // Emit to all vendors in the work area via Socket.IO
            if (global.socketServer) {
                global.socketServer.emitToWorkArea('vendor', workArea, 'vendor-search-request', broadcastData);
                console.log(`📢 Order broadcasted to vendor_${workArea} via Socket.IO`);
            } else {
                console.error('❌ Socket server not available');
                return {
                    success: false,
                    message: 'Socket server not available'
                };
            }

            // Store pending order for timeout handling
            const timeoutId = setTimeout(() => {
                this.handleOrderTimeout(orderId);
            }, this.BROADCAST_TIMEOUT);

            this.pendingOrders.set(orderId, {
                orderDetails: broadcastData,
                timeout: timeoutId,
                broadcastTime: Date.now(),
                targetVendors: availableVendors.map(v => v.vendorId),
                workArea: workArea
            });

            console.log(`✅ Order ${orderId} broadcasted to ${availableVendors.length} vendors via Socket.IO`);
            
            return {
                success: true,
                message: 'Order broadcasted successfully via Socket.IO',
                vendorsNotified: availableVendors.length,
                workArea: workArea,
                expiresAt: broadcastData.expiresAt,
                method: 'socket.io'
            };

        } catch (error) {
            console.error('❌ Error broadcasting order via Socket.IO:', error);
            return {
                success: false,
                message: error.message
            };
        }
    }

    // Handle order timeout
    handleOrderTimeout(orderId) {
        console.log(`⏰ Order ${orderId} timed out - no vendor accepted`);
        const pendingOrder = this.pendingOrders.get(orderId);
        
        if (pendingOrder) {
            // Notify user via Socket.IO
            if (global.socketServer) {
                global.socketServer.emitToUser(orderId, 'order-timeout', {
                    orderId,
                    message: 'No vendor accepted the order within the timeout period',
                    timestamp: new Date()
                });
            }
            
            this.pendingOrders.delete(orderId);
        }
        
        this.emit('orderTimeout', {
            orderId,
            message: 'No vendor accepted the order within the timeout period',
            timestamp: new Date().toISOString()
        });
    }

    // Handle vendor acceptance (called from API)
    handleVendorAcceptance(orderId, vendorId, vendorName) {
        const pendingOrder = this.pendingOrders.get(orderId);
        if (pendingOrder && pendingOrder.timeout) {
            clearTimeout(pendingOrder.timeout);
            this.pendingOrders.delete(orderId);
            console.log(`✅ Cleared timeout for order ${orderId}`);
        }

        this.emit('orderAccepted', {
            orderId,
            vendorId,
            vendorName,
            timestamp: new Date().toISOString()
        });

        console.log(`✅ Vendor ${vendorName} accepted order ${orderId} via Socket.IO`);
    }

    // Get pending orders
    getPendingOrders() {
        const pending = {};
        this.pendingOrders.forEach((order, orderId) => {
            pending[orderId] = {
                broadcastTime: order.broadcastTime,
                timeRemaining: this.BROADCAST_TIMEOUT - (Date.now() - order.broadcastTime),
                targetVendors: order.targetVendors,
                vendorCount: order.targetVendors.length,
                workArea: order.workArea,
                method: 'socket.io'
            };
        });
        return pending;
    }

    // Get service statistics
    getServiceStats() {
        const socketStats = global.socketServer ? global.socketServer.getActiveConnections() : {};
        
        return {
            pendingOrders: this.pendingOrders.size,
            serviceUptime: process.uptime(),
            method: 'socket.io',
            activeConnections: socketStats
        };
    }

    // Health check
    async healthCheck() {
        return {
            healthy: true,
            method: 'socket.io',
            socketServerAvailable: !!global.socketServer,
            stats: this.getServiceStats()
        };
    }
}

module.exports = new OrderBroadcastService();