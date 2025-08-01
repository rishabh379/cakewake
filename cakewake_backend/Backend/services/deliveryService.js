const websocketManager = require('./websocketManager');

class DeliveryService {
    constructor() {
        this.setupWebSocketListeners();
    }

    setupWebSocketListeners() {
        // Listen for delivery updates
        websocketManager.on('deliveryUpdate', (data) => {
            this.handleDeliveryUpdate(data);
        });
    }

    // Request delivery partner for order
    async requestDeliveryPartner(orderData) {
        try {
            if (websocketManager.isConnected('delivery')) {
                return websocketManager.requestDeliveryPartner(orderData);
            } else {
                throw new Error('WebSocket connection to delivery app not available');
            }
        } catch (error) {
            console.error('Error requesting delivery partner:', error);
            throw error;
        }
    }

    // Handle delivery updates
    handleDeliveryUpdate(data) {
        console.log('Delivery update received:', data);
        
        // Update order status in database
        const Order = require('../models/checkout/Order');
        Order.findByIdAndUpdate(data.orderId, {
            status: data.status,
            deliveryPartner: data.deliveryPartner,
            estimatedDeliveryTime: data.estimatedDeliveryTime
        }).exec();
    }
}

module.exports = new DeliveryService();