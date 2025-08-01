const websocketManager = require('../services/websocketManager');

function initializeWebSockets() {
    console.log('🔗 Initializing WebSocket connections...');
    
    // Connect to Vendor App
    websocketManager.connectToVendorApp();
    
    // Connect to Delivery App
    websocketManager.connectToDeliveryApp();
    
    // Setup global event handlers
    websocketManager.on('connected', (type) => {
        console.log(`✅ Successfully connected to ${type} app`);
    });
    
    websocketManager.on('disconnected', (type) => {
        console.log(`❌ Disconnected from ${type} app`);
    });
    
    websocketManager.on('error', (type, error) => {
        console.error(`❌ WebSocket error with ${type}:`, error);
    });
    
    // Graceful shutdown
    process.on('SIGINT', () => {
        console.log('🔌 Closing WebSocket connections...');
        websocketManager.closeAllConnections();
        process.exit(0);
    });
}

module.exports = { initializeWebSockets };