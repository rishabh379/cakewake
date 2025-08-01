const mongoose = require('mongoose');

const orderSchema = new mongoose.Schema({
    // Basic order information
    cakeName: {
        type: String,
        required: true,
        trim: true
    },
    amount: {
        type: Number,
        required: true,
        min: 0
    },
    quantity: {
        type: Number,
        required: true,
        min: 1,
        default: 1
    },
    cakeImage: {
        type: String,
        required: true,
    },
    cakeWeight: {
        type: String,
        required: true,
        trim: true
    },
    customerName: {
        type: String,
        required: true,
        trim: true
    },
    city: {
        type: String,
        required: true,
        trim: true
    },
    area: {
        type: String,
        required: true,
        trim: true
    },
    latitude: {
        type: Number,
        required: true,
    },
    longitude: {
        type: Number,
        required: true,
    },   
    elements: [{
        name: {
            type: String,
            required: true,
            trim: true
        },
        price: {
            type: Number,
            required: true,
            min: 0
        }
    }],
    orderDate: {
        type: Date,
        required: true
    },
    orderTime: {
        type: String,
        required: true
    },
    
    // Customer information
    customer: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    
    // Order status
    status: {
        type: String,
        enum: [
            'DRAFT',
            'PENDING_VENDOR_SELECTION',
            'BROADCASTING_TO_VENDORS',
            'VENDOR_ASSIGNED',
            'PAYMENT_PENDING',
            'PAYMENT_CONFIRMED',
            'PREPARING',
            'READY',
            'COMPLETED',
            'CANCELLED'
        ],
        default: 'DRAFT'
    },
    
    // Vendor selection
    vendorSelectionType: {
        type: String,
        enum: ['USER_SELECT', 'APP_SELECT']
    },
    
    // Assigned vendor details
    assignedVendor: {
        vendorId: String,
        assignedAt: Date
    },

    assignedDeliveryPerson: {
        deliveryPersonId: String,
        assignedAt: Date
    },
    
    // Payment information
    paymentDetails: {
        razorpayOrderId: String,
        razorpayPaymentId: String,
        razorpaySignature: String,
        paymentMethod: String,
        paymentStatus: {
            type: String,
            enum: ['PENDING', 'COMPLETED', 'FAILED'],
            default: 'PENDING'
        },
        paymentDate: Date,
        amount: Number
    },
    
    // Order history - INITIALIZE AS EMPTY ARRAY
    statusHistory: [{
        status: String,
        timestamp: {
            type: Date,
            default: Date.now
        },
        message: String,
        updatedBy: String
    }],

    // Socket.IO related fields (ADD THESE)
    socketEvents: [{
        event: String,
        data: mongoose.Schema.Types.Mixed,
        timestamp: {
            type: Date,
            default: Date.now
        },
        emittedBy: String,
        emittedTo: String
    }],

    // Work area information for Socket.IO rooms
    workArea: {
        city: String,
        area: String,
        room: String
    },

    // Real-time tracking
    liveTracking: {
        isActive: {
            type: Boolean,
            default: false
        },
        deliveryPartnerId: String,
        lastLocationUpdate: {
            latitude: Number,
            longitude: Number,
            timestamp: Date,
            accuracy: Number
        },
        trackingStartedAt: Date,
        trackingRoom: String
    },

    // Communication logs
    communications: [{
        type: {
            type: String,
            enum: ['MESSAGE', 'CALL', 'STATUS_UPDATE', 'NOTIFICATION']
        },
        from: {
            userId: String,
            role: String,
            name: String
        },
        to: {
            userId: String,
            role: String,
            name: String
        },
        content: String,
        timestamp: {
            type: Date,
            default: Date.now
        },
        eventId: String
    }],
    
    // Cancellation details
    cancellationDetails: {
        reason: String,
        cancelledAt: Date,
        cancelledBy: {
            type: String,
            enum: ['CUSTOMER', 'VENDOR', 'SYSTEM']
        }
    }
}, {
    timestamps: true
});

// Update pre-save middleware
orderSchema.pre('save', function(next) {
    // Existing status history logic...
    if (this.isModified('status')) {
        if (!this.statusHistory) {
            this.statusHistory = [];
        }
        
        this.statusHistory.push({
            status: this.status,
            timestamp: new Date(),
            message: `Order status changed to ${this.status}`,
            updatedBy: 'SYSTEM'
        });

        // Emit socket event for status change (NEW)
        if (global.socketServer) {
            const eventData = {
                orderId: this._id,
                status: this.status,
                message: `Order status updated to ${this.status}`,
                timestamp: new Date()
            };

            // Emit to user
            global.socketServer.emitToUser(this.customer.toString(), 'order-status-changed', eventData);
            
            // Emit to assigned vendor
            if (this.assignedVendor?.vendorId) {
                global.socketServer.emitToUser(this.assignedVendor.vendorId, 'order-status-changed', eventData);
            }

            // Emit to assigned delivery partner
            if (this.assignedDeliveryPerson?.deliveryPersonId) {
                global.socketServer.emitToUser(this.assignedDeliveryPerson.deliveryPersonId, 'order-status-changed', eventData);
            }
        }
    }

    // Set work area room if city and area are provided (NEW)
    if (this.city && this.area && !this.workArea?.room) {
        this.workArea = {
            city: this.city,
            area: this.area,
            room: `${this.city}_${this.area}`
        };
    }

    next();
});

module.exports = mongoose.model('Order', orderSchema);