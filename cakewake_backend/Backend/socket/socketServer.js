const socketIo = require('socket.io');
const jwt = require('jsonwebtoken');
const User = require('../models/user/User');

// Import event handlers
const { registerUserHandlers } = require('./events/user');
const { registerVendorHandlers } = require('./events/vendor');
const { registerDeliveryHandlers } = require('./events/delivery');

class SocketServer {
    constructor() {
        this.io = null;
        this.activeConnections = new Map(); // socketId -> userInfo
        this.userSockets = new Map(); // userId -> socketId
    }

    initialize(server) {
        this.io = socketIo(server, {
            cors: {
                origin: process.env.FRONTEND_URL || "http://localhost:3000",
                methods: ["GET", "POST"],
                credentials: true
            }
        });

        // Authentication middleware
        this.io.use(async (socket, next) => {
            try {
                const token = socket.handshake.auth.token;
                if (!token) {
                    return next(new Error('Authentication error: No token provided'));
                }

                const decoded = jwt.verify(token, process.env.JWT_SECRET);
                const user = await User.findById(decoded.userId).populate('profile');
                
                if (!user) {
                    return next(new Error('User not found'));
                }

                // Attach user info to socket
                socket.user = {
                    id: user._id.toString(),
                    role: user.role,
                    profile: user.profile,
                    mobileNumber: user.mobileNumber
                };
                
                console.log(`🔐 Socket authenticated: ${socket.id} (${socket.user.role}:${socket.user.id})`);
                next();
            } catch (error) {
                console.error('Socket authentication error:', error);
                next(new Error('Authentication error'));
            }
        });

        this.io.on('connection', (socket) => {
            this.handleConnection(socket);
        });

        console.log('🚀 Socket.IO server initialized');
        return this.io;
    }

    handleConnection(socket) {
        console.log(`👋 New ${socket.user.role} connected: ${socket.id} (ID: ${socket.user.id})`);

        // Store connection info
        this.activeConnections.set(socket.id, {
            userId: socket.user.id,
            role: socket.user.role,
            connectedAt: new Date(),
            lastActivity: new Date()
        });

        this.userSockets.set(socket.user.id, socket.id);

        // Register event handlers based on role
        this.registerEventHandlers(socket);

        // Handle disconnection
        socket.on('disconnect', () => {
            this.handleDisconnection(socket);
        });

        // Send connection confirmation
        socket.emit('connection-established', {
            userId: socket.user.id,
            role: socket.user.role,
            timestamp: new Date()
        });
    }

    registerEventHandlers(socket) {
        // Update last activity on any event
        socket.use((packet, next) => {
            const connectionInfo = this.activeConnections.get(socket.id);
            if (connectionInfo) {
                connectionInfo.lastActivity = new Date();
            }
            next();
        });

        // Register role-specific event handlers
        switch (socket.user.role) {
            case 'user':
                registerUserHandlers(socket);
                break;
            case 'vendor':
                registerVendorHandlers(socket);
                break;
            case 'delivery':
                registerDeliveryHandlers(socket);
                break;
            default:
                console.warn(`Unknown role: ${socket.user.role}`);
        }
    }

    handleDisconnection(socket) {
        const connectionInfo = this.activeConnections.get(socket.id);
        if (connectionInfo) {
            console.log(`👋 ${connectionInfo.role} ${connectionInfo.userId} disconnected: ${socket.id}`);
            this.activeConnections.delete(socket.id);
            this.userSockets.delete(connectionInfo.userId);
        }
    }

    // Utility methods
    getActiveConnections() {
        const stats = {
            total: this.activeConnections.size,
            users: 0,
            vendors: 0,
            delivery: 0,
            connections: []
        };

        this.activeConnections.forEach((info, socketId) => {
            stats[info.role]++;
            stats.connections.push({
                socketId,
                userId: info.userId,
                role: info.role,
                connectedAt: info.connectedAt,
                lastActivity: info.lastActivity
            });
        });

        return stats;
    }

    emitToUser(userId, event, data) {
        const socketId = this.userSockets.get(userId);
        if (socketId) {
            this.io.to(socketId).emit(event, data);
            return true;
        }
        return false;
    }

    emitToRoom(room, event, data) {
        this.io.to(room).emit(event, data);
    }

    getIO() {
        return this.io;
    }
}

module.exports = new SocketServer();