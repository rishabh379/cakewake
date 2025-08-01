const express = require('express');
const http = require('http');
const socketServer = require('./socket/socketServer');

const app = express();

// Your existing middleware and routes...

// Create HTTP server
const server = http.createServer(app);

// Initialize Socket.IO
const io = socketServer.initialize(server);

// Make socket server available globally
global.io = io;
global.socketServer = socketServer;

// Add socket monitoring route
app.get('/api/socket/status', (req, res) => {
    const stats = socketServer.getActiveConnections();
    res.json({
        success: true,
        socketServer: 'active',
        connections: stats,
        timestamp: new Date()
    });
});

const PORT = process.env.PORT || 4000;

server.listen(PORT, () => {
    console.log(`🚀 Server running on port ${PORT}`);
    console.log(`🔌 Socket.IO ready for connections`);
    console.log(`📊 Socket status: http://localhost:${PORT}/api/socket/status`);
});