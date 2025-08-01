function registerUserHandlers(socket) {
    console.log(`📱 Registering user handlers for user ${socket.user.id}`);

    // 🏠 Event: User joins work area
    socket.on("join-work-area", ({ workArea, workCity }) => {
        const areaRoom = `user_${workCity}_${workArea}`;
        const personalRoom = `user_${socket.user.id}`;
      
        socket.join(areaRoom);       // for broadcast requests
        socket.join(personalRoom);   // for direct, personal updates
      
        console.log(`👤 User ${socket.user.id} joined area room: ${areaRoom}`);
        console.log(`🏠 User ${socket.user.id} joined personal room: ${personalRoom}`);
        
        socket.emit('work-area-joined', {
            workArea,
            workCity,
            areaRoom,
            personalRoom,
            timestamp: new Date()
        });
    });

    // 🔍 Event: User is searching for vendors
    socket.on("search-vendors", ({ workArea, workCity, orderDetails }) => {
        const vendorRoom = `vendor_${workCity}_${workArea}`;
        
        console.log(`🔍 User ${socket.user.id} searching vendors in ${vendorRoom}`);
        console.log(`📦 Order details:`, orderDetails);
        
        // Emit to all vendors in that area
        socket.to(vendorRoom).emit("vendor-search-request", {
            workArea,
            workCity,
            orderDetails,
            userId: socket.user.id,
            customerName: socket.user.profile?.name || 'Customer',
            customerPhone: socket.user.mobileNumber,
            timestamp: new Date()
        });

        console.log(`📢 Search request sent to vendors in ${vendorRoom}`);
        
        // Confirm to user
        socket.emit('vendor-search-initiated', {
            workArea,
            workCity,
            vendorRoom,
            message: 'Search request sent to vendors in your area',
            timestamp: new Date()
        });
    });

    // 📍 Event: User wants to track order
    socket.on("track-order", (orderId, delivery_partner_id) => {
        const trackingRoom = `delivery_partner_${delivery_partner_id}`;
        const orderTrackingRoom = `order_tracking_${orderId}`;
        
        socket.join(trackingRoom);
        socket.join(orderTrackingRoom);
        
        console.log(`📱 User ${socket.user.id} tracking order ${orderId} with delivery partner ${delivery_partner_id}`);
        
        socket.emit('tracking-started', {
            orderId,
            delivery_partner_id,
            trackingRoom,
            orderTrackingRoom,
            message: 'Live tracking started for your order',
            timestamp: new Date()
        });
    });

    // 🎂 Event: User marks order as ready (for testing delivery flow)
    socket.on("order-ready", (orderPayload) => {
        const { workArea, workCity, orderId, orderDetails } = orderPayload;
        const targetRoom = `delivery_partner_${workCity}_${workArea}`;

        console.log(`🎂 User ${socket.user.id} marked order ${orderId} ready`);
        console.log(`📤 Emitting delivery request to ${targetRoom}`);
        
        // Emit to delivery partners in the area
        socket.to(targetRoom).emit("delivery-request", {
            orderId,
            orderDetails,
            workArea,
            workCity,
            fromUser: socket.user.id,
            customerName: socket.user.profile?.name || 'Customer',
            customerPhone: socket.user.mobileNumber,
            timestamp: new Date()
        });

        // Confirm to user
        socket.emit('delivery-request-sent', {
            orderId,
            targetRoom,
            message: 'Delivery request sent to partners in your area',
            timestamp: new Date()
        });
    });

    // 💬 Event: User sends message to vendor
    socket.on("message-to-vendor", ({ orderId, vendorId, message }) => {
        const vendorRoom = `vendor_${vendorId}`;
        
        console.log(`💬 User ${socket.user.id} sending message to vendor ${vendorId} for order ${orderId}`);
        
        socket.to(vendorRoom).emit("customer-message", {
            orderId,
            userId: socket.user.id,
            customerName: socket.user.profile?.name || 'Customer',
            customerPhone: socket.user.mobileNumber,
            message,
            timestamp: new Date()
        });

        // Confirm to user
        socket.emit('message-sent', {
            orderId,
            vendorId,
            message,
            timestamp: new Date()
        });
    });

    // 💬 Event: User sends message to delivery partner
    socket.on("message-to-delivery", ({ orderId, deliveryPartnerId, message }) => {
        const deliveryRoom = `delivery_partner_${deliveryPartnerId}`;
        
        console.log(`💬 User ${socket.user.id} sending message to delivery partner ${deliveryPartnerId} for order ${orderId}`);
        
        socket.to(deliveryRoom).emit("customer-message", {
            orderId,
            userId: socket.user.id,
            customerName: socket.user.profile?.name || 'Customer',
            customerPhone: socket.user.mobileNumber,
            message,
            timestamp: new Date()
        });

        // Confirm to user
        socket.emit('message-sent', {
            orderId,
            deliveryPartnerId,
            message,
            timestamp: new Date()
        });
    });

    // 📞 Event: User calls vendor
    socket.on("call-vendor", ({ orderId, vendorId }) => {
        const vendorRoom = `vendor_${vendorId}`;
        
        console.log(`📞 User ${socket.user.id} calling vendor ${vendorId} for order ${orderId}`);
        
        socket.to(vendorRoom).emit("customer-calling", {
            orderId,
            userId: socket.user.id,
            customerName: socket.user.profile?.name || 'Customer',
            customerPhone: socket.user.mobileNumber,
            timestamp: new Date()
        });

        // Confirm to user
        socket.emit('call-initiated', {
            orderId,
            vendorId,
            message: 'Call request sent to vendor',
            timestamp: new Date()
        });
    });

    // 📞 Event: User calls delivery partner
    socket.on("call-delivery", ({ orderId, deliveryPartnerId }) => {
        const deliveryRoom = `delivery_partner_${deliveryPartnerId}`;
        
        console.log(`📞 User ${socket.user.id} calling delivery partner ${deliveryPartnerId} for order ${orderId}`);
        
        socket.to(deliveryRoom).emit("customer-calling", {
            orderId,
            userId: socket.user.id,
            customerName: socket.user.profile?.name || 'Customer',
            customerPhone: socket.user.mobileNumber,
            timestamp: new Date()
        });

        // Confirm to user
        socket.emit('call-initiated', {
            orderId,
            deliveryPartnerId,
            message: 'Call request sent to delivery partner',
            timestamp: new Date()
        });
    });

    // ❌ Event: User cancels order
    socket.on("cancel-order", ({ orderId, vendorId, deliveryPartnerId, reason }) => {
        console.log(`❌ User ${socket.user.id} cancelling order ${orderId}: ${reason}`);
        
        // Notify vendor if assigned
        if (vendorId) {
            socket.to(`vendor_${vendorId}`).emit("order-cancelled-by-customer", {
                orderId,
                userId: socket.user.id,
                customerName: socket.user.profile?.name || 'Customer',
                reason,
                message: 'Order has been cancelled by the customer',
                timestamp: new Date()
            });
        }

        // Notify delivery partner if assigned
        if (deliveryPartnerId) {
            socket.to(`delivery_partner_${deliveryPartnerId}`).emit("order-cancelled-by-customer", {
                orderId,
                userId: socket.user.id,
                customerName: socket.user.profile?.name || 'Customer',
                reason,
                message: 'Order has been cancelled by the customer',
                timestamp: new Date()
            });
        }

        // Confirm to user
        socket.emit('order-cancel-initiated', {
            orderId,
            reason,
            message: 'Order cancellation initiated',
            timestamp: new Date()
        });
    });

    // 🔔 Event: User subscribes to order updates
    socket.on("subscribe-order-updates", (orderId) => {
        const orderRoom = `order_${orderId}`;
        socket.join(orderRoom);
        
        console.log(`🔔 User ${socket.user.id} subscribed to updates for order ${orderId}`);
        
        socket.emit('subscribed-to-order', {
            orderId,
            orderRoom,
            message: 'Subscribed to order updates',
            timestamp: new Date()
        });
    });

    // 📊 Event: User requests status update
    socket.on("request-status-update", (orderId) => {
        console.log(`📊 User ${socket.user.id} requesting status for order ${orderId}`);
        
        // This would typically fetch from database and emit back
        socket.emit('status-update-requested', {
            orderId,
            message: 'Status update requested',
            timestamp: new Date()
        });
    });

    // ⭐ Event: User rates vendor
    socket.on("rate-vendor", ({ orderId, vendorId, rating, review }) => {
        const vendorRoom = `vendor_${vendorId}`;
        
        console.log(`⭐ User ${socket.user.id} rating vendor ${vendorId} for order ${orderId}: ${rating}/5`);
        
        socket.to(vendorRoom).emit("customer-rating", {
            orderId,
            userId: socket.user.id,
            customerName: socket.user.profile?.name || 'Customer',
            rating,
            review,
            timestamp: new Date()
        });

        // Confirm to user
        socket.emit('rating-submitted', {
            orderId,
            vendorId,
            rating,
            review,
            message: 'Rating submitted successfully',
            timestamp: new Date()
        });
    });

    // ⭐ Event: User rates delivery partner
    socket.on("rate-delivery", ({ orderId, deliveryPartnerId, rating, review }) => {
        const deliveryRoom = `delivery_partner_${deliveryPartnerId}`;
        
        console.log(`⭐ User ${socket.user.id} rating delivery partner ${deliveryPartnerId} for order ${orderId}: ${rating}/5`);
        
        socket.to(deliveryRoom).emit("customer-rating", {
            orderId,
            userId: socket.user.id,
            customerName: socket.user.profile?.name || 'Customer',
            rating,
            review,
            timestamp: new Date()
        });

        // Confirm to user
        socket.emit('rating-submitted', {
            orderId,
            deliveryPartnerId,
            rating,
            review,
            message: 'Rating submitted successfully',
            timestamp: new Date()
        });
    });

    // 🆘 Event: User reports issue
    socket.on("report-issue", ({ orderId, vendorId, deliveryPartnerId, issue, category, severity }) => {
        console.log(`🆘 User ${socket.user.id} reporting issue for order ${orderId}: ${issue}`);
        
        // Notify vendor if applicable
        if (vendorId) {
            socket.to(`vendor_${vendorId}`).emit("customer-issue-reported", {
                orderId,
                userId: socket.user.id,
                customerName: socket.user.profile?.name || 'Customer',
                issue,
                category,
                severity,
                timestamp: new Date()
            });
        }

        // Notify delivery partner if applicable
        if (deliveryPartnerId) {
            socket.to(`delivery_partner_${deliveryPartnerId}`).emit("customer-issue-reported", {
                orderId,
                userId: socket.user.id,
                customerName: socket.user.profile?.name || 'Customer',
                issue,
                category,
                severity,
                timestamp: new Date()
            });
        }

        // Confirm to user
        socket.emit('issue-reported-confirmed', {
            orderId,
            issue,
            category,
            severity,
            message: 'Issue reported successfully. We will look into it.',
            timestamp: new Date()
        });
    });

    // 🚨 Event: User needs emergency assistance
    socket.on("emergency-help", ({ orderId, emergency, location }) => {
        console.log(`🚨 User ${socket.user.id} requesting emergency help for order ${orderId}`);
        
        // This could notify admin/support team
        socket.emit('emergency-acknowledged', {
            orderId,
            emergency,
            location,
            message: 'Emergency request received. Help is on the way.',
            timestamp: new Date()
        });
    });
}

module.exports = { registerUserHandlers };