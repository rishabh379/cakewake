const WebSocket = require('ws');
const EventEmitter = require('events');

class WebSocketManager extends EventEmitter {
    constructor() {
        super();
        this.connections = new Map();
        this.vendorConnections = new Map(); // Dedicated vendor connections
        this.reconnectAttempts = new Map();
        this.maxReconnectAttempts = 5;
        this.reconnectDelay = 5000;
    }

    // Establish dedicated connection with specific vendor
    establishVendorConnection(vendorId) {
        try {
            const vendorWsUrl = `${process.env.VENDOR_APP_WS_URL}/vendor/${vendorId}`;
            const ws = new WebSocket(vendorWsUrl, {
                headers: {
                    'Authorization': `Bearer ${process.env.VENDOR_APP_API_KEY}`,
                    'App-Type': 'USER_APP',
                    'Vendor-Id': vendorId
                }
            });

            ws.on('open', () => {
                console.log(`🔗 Dedicated connection established with vendor ${vendorId}`);
                this.vendorConnections.set(vendorId, ws);
                
                // Send handshake
                ws.send(JSON.stringify({
                    type: 'VENDOR_HANDSHAKE',
                    data: { vendorId, appType: 'USER_APP' },
                    timestamp: new Date().toISOString()
                }));
            });

            ws.on('message', (data) => {
                try {
                    const message = JSON.parse(data);
                    this.handleVendorMessage(vendorId, message);
                } catch (error) {
                    console.error(`Error parsing message from vendor ${vendorId}:`, error);
                }
            });

            ws.on('close', () => {
                console.log(`❌ Dedicated connection closed with vendor ${vendorId}`);
                this.vendorConnections.delete(vendorId);
            });

            ws.on('error', (error) => {
                console.error(`WebSocket error with vendor ${vendorId}:`, error);
            });

        } catch (error) {
            console.error(`Failed to establish connection with vendor ${vendorId}:`, error);
        }
    }

    // Close dedicated vendor connection
    closeVendorConnection(vendorId) {
        const connection = this.vendorConnections.get(vendorId);
        if (connection) {
            connection.close();
            this.vendorConnections.delete(vendorId);
            console.log(`🔌 Closed dedicated connection with vendor ${vendorId}`);
        }
    }

    // Handle messages from dedicated vendor connections
    handleVendorMessage(vendorId, message) {
        console.log(`📨 Message from vendor ${vendorId}:`, message);
        
        switch (message.type) {
            case 'ORDER_STATUS_UPDATE':
                this.emit('orderStatusUpdate', { vendorId, ...message.data });
                break;
            case 'ORDER_READY':
                this.emit('orderReady', { vendorId, ...message.data });
                break;
            case 'VENDOR_MESSAGE':
                this.emit('vendorMessage', { vendorId, ...message.data });
                break;
            default:
                console.log(`Unknown message type from vendor ${vendorId}:`, message.type);
        }
    }

    // Send message to specific vendor
    sendMessageToVendor(vendorId, message) {
        const connection = this.vendorConnections.get(vendorId);
        if (connection && connection.readyState === WebSocket.OPEN) {
            connection.send(JSON.stringify(message));
            return true;
        } else {
            console.error(`❌ Cannot send message to vendor ${vendorId} - connection not available`);
            return false;
        }
    }

    // Connect to Vendor App Backend (general connection)
    connectToVendorApp() {
        const vendorWsUrl = process.env.VENDOR_APP_WS_URL || 'ws://localhost:5000';
        this.createConnection('vendor', vendorWsUrl, {
            'Authorization': `Bearer ${process.env.VENDOR_APP_API_KEY}`,
            'App-Type': 'USER_APP'
        });
    }

    // Connect to Delivery Partner App Backend
    connectToDeliveryApp() {
        const deliveryWsUrl = process.env.DELIVERY_APP_WS_URL || 'ws://localhost:6000';
        this.createConnection('delivery', deliveryWsUrl, {
            'Authorization': `Bearer ${process.env.DELIVERY_APP_API_KEY}`,
            'App-Type': 'USER_APP'
        });
    }

    // Generic connection creator (existing code)
    createConnection(type, url, headers) {
        try {
            const ws = new WebSocket(url, { headers });
            
            ws.on('open', () => {
                console.log(`✅ Connected to ${type} app backend`);
                this.connections.set(type, ws);
                this.reconnectAttempts.set(type, 0);
                this.emit('connected', type);
                
                // Send initial handshake
                this.sendMessage(type, {
                    type: 'HANDSHAKE',
                    appType: 'USER_APP',
                    timestamp: new Date().toISOString()
                });
            });

            ws.on('message', (data) => {
                try {
                    const message = JSON.parse(data);
                    this.handleMessage(type, message);
                } catch (error) {
                    console.error(`Error parsing message from ${type}:`, error);
                }
            });

            ws.on('close', () => {
                console.log(`❌ Disconnected from ${type} app backend`);
                this.connections.delete(type);
                this.emit('disconnected', type);
                this.scheduleReconnect(type, url, headers);
            });

            ws.on('error', (error) => {
                console.error(`WebSocket error with ${type}:`, error);
                this.emit('error', type, error);
            });

        } catch (error) {
            console.error(`Failed to create connection to ${type}:`, error);
            this.scheduleReconnect(type, url, headers);
        }
    }

    // Handle incoming messages (existing code)
    handleMessage(source, message) {
        console.log(`📨 Message from ${source}:`, message);
        
        switch (message.type) {
            case 'VENDOR_RESPONSE':
                this.handleVendorResponse(message.data);
                break;
            case 'ORDER_STATUS_UPDATE':
                this.handleOrderStatusUpdate(message.data);
                break;
            case 'DELIVERY_UPDATE':
                this.handleDeliveryUpdate(message.data);
                break;
            case 'VENDOR_AVAILABILITY':
                this.handleVendorAvailability(message.data);
                break;
            case 'HEARTBEAT':
                this.sendHeartbeat(source);
                break;
            default:
                console.log(`Unknown message type from ${source}:`, message.type);
        }
    }

    // Send message to specific app (existing code)
    sendMessage(target, message) {
        const connection = this.connections.get(target);
        if (connection && connection.readyState === WebSocket.OPEN) {
            connection.send(JSON.stringify(message));
            return true;
        } else {
            console.error(`❌ Cannot send message to ${target} - connection not available`);
            return false;
        }
    }

    // Broadcast order to vendors
    broadcastOrderToVendors(orderData) {
        return this.sendMessage('vendor', {
            type: 'BROADCAST_ORDER',
            data: orderData,
            timestamp: new Date().toISOString()
        });
    }

    // Assign order to specific vendor
    assignOrderToVendor(orderId, vendorId) {
        return this.sendMessage('vendor', {
            type: 'ASSIGN_ORDER',
            data: { orderId, vendorId },
            timestamp: new Date().toISOString()
        });
    }

    // Request delivery partner for order
    requestDeliveryPartner(orderData) {
        return this.sendMessage('delivery', {
            type: 'REQUEST_DELIVERY',
            data: orderData,
            timestamp: new Date().toISOString()
        });
    }

    // Get available vendors by location
    getAvailableVendors(city, area) {
        return this.sendMessage('vendor', {
            type: 'GET_AVAILABLE_VENDORS',
            data: { city, area },
            timestamp: new Date().toISOString()
        });
    }

    // Handle vendor response to order broadcast
    handleVendorResponse(data) {
        this.emit('vendorResponse', data);
    }

    // Handle order status updates
    handleOrderStatusUpdate(data) {
        this.emit('orderStatusUpdate', data);
    }

    // Handle delivery updates
    handleDeliveryUpdate(data) {
        this.emit('deliveryUpdate', data);
    }

    // Handle vendor availability updates
    handleVendorAvailability(data) {
        this.emit('vendorAvailability', data);
    }

    // Send heartbeat response
    sendHeartbeat(target) {
        this.sendMessage(target, {
            type: 'HEARTBEAT_RESPONSE',
            timestamp: new Date().toISOString()
        });
    }

    // Schedule reconnection
    scheduleReconnect(type, url, headers) {
        const attempts = this.reconnectAttempts.get(type) || 0;
        
        if (attempts < this.maxReconnectAttempts) {
            const delay = this.reconnectDelay * Math.pow(2, attempts);
            console.log(`🔄 Scheduling reconnect to ${type} in ${delay}ms (attempt ${attempts + 1})`);
            
            setTimeout(() => {
                this.reconnectAttempts.set(type, attempts + 1);
                this.createConnection(type, url, headers);
            }, delay);
        } else {
            console.error(`❌ Max reconnection attempts reached for ${type}`);
        }
    }

    // Check connection status
    isConnected(type) {
        const connection = this.connections.get(type);
        return connection && connection.readyState === WebSocket.OPEN;
    }

    // Check vendor connection status
    isVendorConnected(vendorId) {
        const connection = this.vendorConnections.get(vendorId);
        return connection && connection.readyState === WebSocket.OPEN;
    }

    // Get all connection statuses
    getConnectionStatuses() {
        return {
            vendor: this.isConnected('vendor'),
            delivery: this.isConnected('delivery'),
            connections: Array.from(this.connections.keys()),
            dedicatedVendorConnections: Array.from(this.vendorConnections.keys())
        };
    }

    // Close all connections
    closeAllConnections() {
        this.connections.forEach((connection, type) => {
            console.log(`Closing connection to ${type}`);
            connection.close();
        });
        this.connections.clear();

        this.vendorConnections.forEach((connection, vendorId) => {
            console.log(`Closing dedicated connection to vendor ${vendorId}`);
            connection.close();
        });
        this.vendorConnections.clear();
    }
}

module.exports = new WebSocketManager();