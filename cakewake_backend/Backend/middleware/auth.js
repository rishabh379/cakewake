const jwt = require('jsonwebtoken');
const User = require('../models/user/User');
require('dotenv').config();

// Middleware to verify token
exports.auth = async (req, res, next) => {
    try {
        // Extract JWT token from cookies, body, or headers
        const token = req.headers.authorization?.split(' ')[1] || req.cookies.token || req.body.token;
        
        if (!token) {
            return res.status(401).json({
                success: false,
                message: 'Token Missing',
            });
        }

        // Verify the token
        try {
            const payload = jwt.verify(token, process.env.JWT_SECRET);
            
            // Fetch complete user data for Socket.IO operations
            const user = await User.findById(payload.id).populate('profile');
            if (!user) {
                return res.status(401).json({
                    success: false,
                    message: 'User not found',
                });
            }

            req.user = {
                id: user._id.toString(),
                role: user.role,
                name: user.profile?.name || user.mobileNumber,
                mobileNumber: user.mobileNumber,
                profile: user.profile
            };
        } catch (error) {
            return res.status(401).json({
                success: false,
                message: 'Token is invalid',
            });
        }
        next();
    } catch (error) {
        return res.status(401).json({
            success: false,
            message: 'Something went wrong while verifying the token',
            error: error.message,
        });
    }
};

// Middleware to check if the user is a regular user
exports.isUser = (req, res, next) => {
    try {
        if (req.user.role !== 'user') {
            return res.status(401).json({
                success: false,
                message: 'This is a protected route for users',
            });
        }
        next();
    } catch (error) {
        return res.status(500).json({
            success: false,
            message: 'User role is not matching',
        });
    }
};

// Middleware to check if the user is an admin
exports.isAdmin = (req, res, next) => {
    try {
        if (req.user.role !== 'admin') {
            return res.status(401).json({
                success: false,
                message: 'This is a protected route for admins',
            });
        }
        next();
    } catch (error) {
        return res.status(500).json({
            success: false,
            message: 'User role is not matching',
        });
    }
};

// Middleware to check if the user is a vendor
exports.isVendor = (req, res, next) => {
    try {
        if (req.user.role !== 'vendor') {
            return res.status(401).json({
                success: false,
                message: 'This is a protected route for vendors',
            });
        }
        next();
    } catch (error) {
        return res.status(500).json({
            success: false,
            message: 'User role is not matching',
        });
    }
};

// NEW: Middleware to check if the user is a delivery partner
exports.isDelivery = (req, res, next) => {
    try {
        if (req.user.role !== 'delivery') {
            return res.status(401).json({
                success: false,
                message: 'This is a protected route for delivery partners',
            });
        }
        next();
    } catch (error) {
        return res.status(500).json({
            success: false,
            message: 'User role is not matching',
        });
    }
};

// NEW: Middleware to check multiple roles
exports.isAnyOf = (roles) => {
    return (req, res, next) => {
        try {
            if (!roles.includes(req.user.role)) {
                return res.status(401).json({
                    success: false,
                    message: `This route is protected. Allowed roles: ${roles.join(', ')}`,
                });
            }
            next();
        } catch (error) {
            return res.status(500).json({
                success: false,
                message: 'Role validation failed',
            });
        }
    };
};

// NEW: Middleware to check order ownership/permission
exports.canAccessOrder = async (req, res, next) => {
    try {
        const orderId = req.params.orderId || req.body.orderId;
        const userId = req.user.id;
        const userRole = req.user.role;

        if (!orderId) {
            return res.status(400).json({
                success: false,
                message: 'Order ID is required',
            });
        }

        const Order = require('../models/checkout/Order');
        const order = await Order.findById(orderId);

        if (!order) {
            return res.status(404).json({
                success: false,
                message: 'Order not found',
            });
        }

        // Check permissions based on role
        let hasAccess = false;

        switch (userRole) {
            case 'user':
                hasAccess = order.customer.toString() === userId;
                break;
            case 'vendor':
                hasAccess = order.assignedVendor?.vendorId === userId;
                break;
            case 'delivery':
                hasAccess = order.assignedDeliveryPerson?.deliveryPersonId === userId;
                break;
            case 'admin':
                hasAccess = true; // Admin can access all orders
                break;
        }

        if (!hasAccess) {
            return res.status(403).json({
                success: false,
                message: 'You do not have permission to access this order',
            });
        }

        req.order = order; // Attach order to request for use in controller
        next();
    } catch (error) {
        return res.status(500).json({
            success: false,
            message: 'Order access validation failed',
            error: error.message
        });
    }
};

exports.isCakeOwner = async (req, res, next) => {
    try {
        const cakeId = req.params.id;
        const userId = req.user.id;

        const CakeDesign = require('../models/cake/CakeDesign');
        const cake = await CakeDesign.findById(cakeId);
        if (!cake) {
            return res.status(404).json({ success: false, message: 'Cake design not found' });
        }

        if (cake.owner.toString() !== userId) {
            return res.status(403).json({ success: false, message: 'Not authorized to modify this cake design' });
        }

        next();
    } catch (error) {
        return res.status(500).json({ success: false, message: 'Authorization failed', error: error.message });
    }
};