const express = require("express");
const http = require('http');
const app = express();

require('dotenv').config();
const PORT = process.env.PORT || 4000;

//cookie-parser

const cookieParser = require("cookie-parser");
app.use(cookieParser());

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

const fileUpload = require('express-fileupload');
app.use(fileUpload({
    useTempFiles: true,
    tempFileDir: '/tmp/'
}));

require("./config/database").connect();

// Initialize Socket.IO Server (NEW)
const socketServer = require('./socket/socketServer');

// Initialize Order Helper Service
const OrderHelperService = require('./services/orderHelperService');

// Initialize Order Broadcast Service (Updated for Socket.IO)
const orderBroadcastService = require('./services/orderBroadcastService');

// Set up event listeners for order broadcast service
orderBroadcastService.on('orderAccepted', (data) => {
    console.log('✅ Order accepted via Socket.IO:', data);
    // Additional logic for order acceptance
});

orderBroadcastService.on('orderStatusUpdate', (data) => {
    console.log('📊 Order status update via Socket.IO:', data);
    // Additional logic for status updates
});

orderBroadcastService.on('orderTimeout', (data) => {
    console.log('⏰ Order timeout via Socket.IO:', data);
    // Additional logic for order timeout
});

const { auth } = require('./middleware/auth');

// Add vendor notification routes
const vendorNotificationRoutes = require('./routes/vendorNotification');
app.use('/api/vendor-notification', vendorNotificationRoutes);

const cakeRoutes = require('./routes/cake');
app.use('/api/v1/cake', cakeRoutes);

const cakeIngredientRoutes = require('./routes/cakeIngredient');
app.use('/api/v1/ingredients', cakeIngredientRoutes);

// Add order routes
const orderRoutes = require('./routes/order');
app.use('/api/v1/orders', orderRoutes);

// Apply auth middleware only to protected routes
const profileRoutes = require('./routes/profile');
app.use('/api/v1/profile', auth, profileRoutes);

const locationRoutes = require('./routes/location');
app.use('/api/v1/location', auth, locationRoutes);

//route import and mount
const user = require("./routes/user");
app.use("/api/v1", user);


// Socket.IO status endpoint (UPDATED)
app.get('/api/v1/socket/status', (req, res) => {
    const stats = socketServer.getActiveConnections();
    res.json({
        success: true,
        method: 'socket.io',
        connections: stats,
        broadcastService: orderBroadcastService.getServiceStats(),
        timestamp: new Date()
    });
});

// Socket.IO health check endpoint (NEW)
app.get('/api/v1/socket/health', async (req, res) => {
    const health = await orderBroadcastService.healthCheck();
    res.json(health);
});

// Get pending orders via Socket.IO (NEW)
app.get('/api/v1/socket/pending-orders', (req, res) => {
    const pendingOrders = orderBroadcastService.getPendingOrders();
    res.json({
        success: true,
        pendingOrders,
        count: Object.keys(pendingOrders).length,
        timestamp: new Date()
    });
});

// Create HTTP server and initialize Socket.IO (NEW)
const server = http.createServer(app);

// Initialize Socket.IO
const io = socketServer.initialize(server);

// Make socket server available globally
global.io = io;
global.socketServer = socketServer;

console.log('🔌 Socket.IO server initialized');

// Start server (UPDATED)
server.listen(PORT, () => {
    console.log(`🚀 Server is listening at ${PORT}`);
    console.log(`🔌 Socket.IO ready for connections`);
    console.log(`📊 Socket status: http://localhost:${PORT}/api/v1/socket/status`);
    console.log(`🏥 Socket health: http://localhost:${PORT}/api/v1/socket/health`);
});