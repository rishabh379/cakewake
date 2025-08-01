function registerVendorHandlers(socket) {
    console.log(`🏪 Registering vendor handlers for vendor ${socket.user.id}`);

    // 🏠 Event: Vendor joins work area
    socket.on("join-work-area", ({ workArea, workCity }) => {
        const areaRoom = `vendor_${workCity}_${workArea}`;
        const personalRoom = `vendor_${socket.user.id}`;
      
        socket.join(areaRoom);       // for receiving search requests
        socket.join(personalRoom);   // for direct updates
      
        console.log(`🏪 Vendor ${socket.user.id} joined area room: ${areaRoom}`);
        console.log(`🏠 Vendor ${socket.user.id} joined personal room: ${personalRoom}`);
        
        socket.emit('work-area-joined', {
            workArea,
            workCity,
            areaRoom,
            personalRoom,
            timestamp: new Date()
        });
    });

    // ✅ Event: Vendor accepts an order
    socket.on("accept-order", ({ orderId, userId, orderDetails }) => {
        const userRoom = `user_${userId}`;
        
        console.log(`✅ Vendor ${socket.user.id} accepting order ${orderId} for user ${userId}`);
        
        // Notify the user
        socket.to(userRoom).emit("vendor-order-accepted", {
            orderId,
            vendorId: socket.user.id,
            vendorName: socket.user.profile?.businessDetail?.businessName || 'Vendor',
            vendorPhone: socket.user.mobileNumber,
            orderDetails,
            message: 'Your order has been accepted by the vendor!',
            timestamp: new Date()
        });

        // Confirm to vendor
        socket.emit('order-accepted-confirmed', {
            orderId,
            userId,
            message: 'Order acceptance confirmed',
            timestamp: new Date()
        });

        console.log(`📤 Order acceptance sent to user ${userId}`);
    });

    // 🚫 Event: Vendor rejects an order
    socket.on("reject-order", ({ orderId, userId, reason }) => {
        const userRoom = `user_${userId}`;
        
        console.log(`🚫 Vendor ${socket.user.id} rejecting order ${orderId}: ${reason}`);
        
        // Notify the user
        socket.to(userRoom).emit("vendor-order-rejected", {
            orderId,
            vendorId: socket.user.id,
            vendorName: socket.user.profile?.businessDetail?.businessName || 'Vendor',
            reason,
            message: 'Your order was declined by the vendor',
            timestamp: new Date()
        });

        // Confirm to vendor
        socket.emit('order-rejected-confirmed', {
            orderId,
            userId,
            reason,
            timestamp: new Date()
        });
    });

    // 🎂 Event: Vendor marks order as ready for pickup/delivery
    socket.on("order-ready", ({ orderId, userId, workArea, workCity, orderDetails }) => {
        const deliveryRoom = `delivery_partner_${workCity}_${workArea}`;
        const userRoom = `user_${userId}`;
        
        console.log(`🎂 Vendor ${socket.user.id} marked order ${orderId} ready`);
        console.log(`📤 Notifying delivery partners in ${deliveryRoom}`);
        
        // Notify delivery partners
        socket.to(deliveryRoom).emit("delivery-request", {
            orderId,
            vendorId: socket.user.id,
            vendorName: socket.user.profile?.businessDetail?.businessName || 'Vendor',
            vendorPhone: socket.user.mobileNumber,
            vendorAddress: socket.user.profile?.businessDetail?.address || 'Vendor Location',
            userId,
            workArea,
            workCity,
            orderDetails,
            message: 'New delivery request available',
            timestamp: new Date()
        });

        // Notify user
        socket.to(userRoom).emit("order-ready-for-delivery", {
            orderId,
            vendorId: socket.user.id,
            vendorName: socket.user.profile?.businessDetail?.businessName || 'Vendor',
            message: 'Your order is ready and delivery partner is being assigned',
            timestamp: new Date()
        });

        // Confirm to vendor
        socket.emit('delivery-request-sent', {
            orderId,
            deliveryRoom,
            message: 'Delivery request sent to partners',
            timestamp: new Date()
        });
    });

    // 🔄 Event: Vendor updates order status
    socket.on("update-order-status", ({ orderId, userId, status, message }) => {
        const userRoom = `user_${userId}`;
        const orderRoom = `order_${orderId}`;
        
        console.log(`🔄 Vendor ${socket.user.id} updating order ${orderId} status to ${status}`);
        
        // Notify user and anyone subscribed to order updates
        socket.to(userRoom).emit("order-status-updated", {
            orderId,
            vendorId: socket.user.id,
            vendorName: socket.user.profile?.businessDetail?.businessName || 'Vendor',
            status,
            message,
            timestamp: new Date()
        });

        socket.to(orderRoom).emit("order-status-updated", {
            orderId,
            vendorId: socket.user.id,
            status,
            message,
            timestamp: new Date()
        });

        // Confirm to vendor
        socket.emit('status-update-confirmed', {
            orderId,
            status,
            message,
            timestamp: new Date()
        });
    });

    // 👥 Event: Vendor starts preparing order
    socket.on("start-preparation", ({ orderId, userId, estimatedTime }) => {
        const userRoom = `user_${userId}`;
        const orderRoom = `order_${orderId}`;
        
        console.log(`👥 Vendor ${socket.user.id} starting preparation for order ${orderId}`);
        
        // Notify user
        socket.to(userRoom).emit("order-preparation-started", {
            orderId,
            vendorId: socket.user.id,
            vendorName: socket.user.profile?.businessDetail?.businessName || 'Vendor',
            estimatedTime,
            message: `Your order preparation has started. Estimated time: ${estimatedTime} minutes`,
            timestamp: new Date()
        });

        socket.to(orderRoom).emit("order-preparation-started", {
            orderId,
            vendorId: socket.user.id,
            estimatedTime,
            timestamp: new Date()
        });

        // Confirm to vendor
        socket.emit('preparation-started-confirmed', {
            orderId,
            estimatedTime,
            message: 'Order preparation started',
            timestamp: new Date()
        });
    });

    // 💬 Event: Vendor sends message to customer
    socket.on("message-to-customer", ({ orderId, userId, message }) => {
        const userRoom = `user_${userId}`;
        
        console.log(`💬 Vendor ${socket.user.id} sending message to user ${userId} for order ${orderId}`);
        
        socket.to(userRoom).emit("vendor-message", {
            orderId,
            vendorId: socket.user.id,
            vendorName: socket.user.profile?.businessDetail?.businessName || 'Vendor',
            message,
            timestamp: new Date()
        });

        // Confirm to vendor
        socket.emit('message-sent', {
            orderId,
            userId,
            message,
            timestamp: new Date()
        });
    });

    // 💬 Event: Vendor sends message to delivery partner
    socket.on("message-to-delivery", ({ orderId, deliveryPartnerId, message }) => {
        const deliveryRoom = `delivery_partner_${deliveryPartnerId}`;
        
        console.log(`💬 Vendor ${socket.user.id} sending message to delivery partner ${deliveryPartnerId} for order ${orderId}`);
        
        socket.to(deliveryRoom).emit("vendor-message", {
            orderId,
            vendorId: socket.user.id,
            vendorName: socket.user.profile?.businessDetail?.businessName || 'Vendor',
            message,
            timestamp: new Date()
        });

        // Confirm to vendor
        socket.emit('message-sent', {
            orderId,
            deliveryPartnerId,
            message,
            timestamp: new Date()
        });
    });

    // 📞 Event: Vendor calls customer
    socket.on("call-customer", ({ orderId, userId }) => {
        const userRoom = `user_${userId}`;
        
        console.log(`📞 Vendor ${socket.user.id} calling user ${userId} for order ${orderId}`);
        
        socket.to(userRoom).emit("vendor-calling", {
            orderId,
            vendorId: socket.user.id,
            vendorName: socket.user.profile?.businessDetail?.businessName || 'Vendor',
            vendorPhone: socket.user.mobileNumber,
            timestamp: new Date()
        });

        // Confirm to vendor
        socket.emit('call-initiated', {
            orderId,
            userId,
            message: 'Call request sent to customer',
            timestamp: new Date()
        });
    });

    // 📞 Event: Vendor calls delivery partner
    socket.on("call-delivery", ({ orderId, deliveryPartnerId }) => {
        const deliveryRoom = `delivery_partner_${deliveryPartnerId}`;
        
        console.log(`📞 Vendor ${socket.user.id} calling delivery partner ${deliveryPartnerId} for order ${orderId}`);
        
        socket.to(deliveryRoom).emit("vendor-calling", {
            orderId,
            vendorId: socket.user.id,
            vendorName: socket.user.profile?.businessDetail?.businessName || 'Vendor',
            vendorPhone: socket.user.mobileNumber,
            timestamp: new Date()
        });

        // Confirm to vendor
        socket.emit('call-initiated', {
            orderId,
            deliveryPartnerId,
            message: 'Call request sent to delivery partner',
            timestamp: new Date()
        });
    });

    // 🕒 Event: Vendor updates delivery time estimate
    socket.on("update-delivery-time", ({ orderId, userId, estimatedTime, message }) => {
        const userRoom = `user_${userId}`;
        const orderRoom = `order_${orderId}`;
        
        console.log(`🕒 Vendor ${socket.user.id} updating delivery time for order ${orderId}`);
        
        socket.to(userRoom).emit("delivery-time-updated", {
            orderId,
            vendorId: socket.user.id,
            vendorName: socket.user.profile?.businessDetail?.businessName || 'Vendor',
            estimatedTime,
            message,
            timestamp: new Date()
        });

        socket.to(orderRoom).emit("delivery-time-updated", {
            orderId,
            vendorId: socket.user.id,
            estimatedTime,
            message,
            timestamp: new Date()
        });

        // Confirm to vendor
        socket.emit('delivery-time-updated-confirmed', {
            orderId,
            estimatedTime,
            message,
            timestamp: new Date()
        });
    });

    // 🏪 Event: Vendor updates availability status
    socket.on("update-availability", ({ isAvailable, reason }) => {
        console.log(`🏪 Vendor ${socket.user.id} updating availability: ${isAvailable ? 'Available' : 'Unavailable'}`);
        
        // Confirm to vendor
        socket.emit('availability-updated', {
            isAvailable,
            reason,
            message: `Status updated to ${isAvailable ? 'Available' : 'Unavailable'}`,
            timestamp: new Date()
        });
    });

    // ❌ Event: Vendor cancels order
    socket.on("cancel-order", ({ orderId, userId, deliveryPartnerId, reason }) => {
        console.log(`❌ Vendor ${socket.user.id} cancelling order ${orderId}: ${reason}`);
        
        // Notify customer
        socket.to(`user_${userId}`).emit("order-cancelled-by-vendor", {
            orderId,
            vendorId: socket.user.id,
            vendorName: socket.user.profile?.businessDetail?.businessName || 'Vendor',
            reason,
            message: 'Your order has been cancelled by the vendor',
            timestamp: new Date()
        });

        // Notify delivery partner if assigned
        if (deliveryPartnerId) {
            socket.to(`delivery_partner_${deliveryPartnerId}`).emit("order-cancelled-by-vendor", {
                orderId,
                vendorId: socket.user.id,
                vendorName: socket.user.profile?.businessDetail?.businessName || 'Vendor',
                reason,
                message: 'Order has been cancelled by the vendor',
                timestamp: new Date()
            });
        }

        // Confirm to vendor
        socket.emit('order-cancel-confirmed', {
            orderId,
            reason,
            message: 'Order cancellation confirmed',
            timestamp: new Date()
        });
    });

    // 📊 Event: Vendor requests order analytics
    socket.on("request-analytics", ({ period, metrics }) => {
        console.log(`📊 Vendor ${socket.user.id} requesting analytics for period: ${period}`);
        
        // This would typically fetch analytics from database
        socket.emit('analytics-data', {
            period,
            metrics,
            message: 'Analytics data fetched',
            timestamp: new Date()
        });
    });

    // 🆘 Event: Vendor reports issue
    socket.on("report-issue", ({ orderId, userId, deliveryPartnerId, issue, category, severity }) => {
        console.log(`🆘 Vendor ${socket.user.id} reporting issue for order ${orderId}: ${issue}`);
        
        // Notify customer if applicable
        if (userId) {
            socket.to(`user_${userId}`).emit("vendor-issue-reported", {
                orderId,
                vendorId: socket.user.id,
                vendorName: socket.user.profile?.businessDetail?.businessName || 'Vendor',
                issue,
                category,
                severity,
                timestamp: new Date()
            });
        }

        // Notify delivery partner if applicable
        if (deliveryPartnerId) {
            socket.to(`delivery_partner_${deliveryPartnerId}`).emit("vendor-issue-reported", {
                orderId,
                vendorId: socket.user.id,
                vendorName: socket.user.profile?.businessDetail?.businessName || 'Vendor',
                issue,
                category,
                severity,
                timestamp: new Date()
            });
        }

        // Confirm to vendor
        socket.emit('issue-reported-confirmed', {
            orderId,
            issue,
            category,
            severity,
            message: 'Issue reported successfully',
            timestamp: new Date()
        });
    });

    // 📋 Event: Vendor updates menu/catalog
    socket.on("update-menu", ({ items, action }) => {
        console.log(`📋 Vendor ${socket.user.id} updating menu: ${action}`);
        
        // Confirm to vendor
        socket.emit('menu-updated', {
            items,
            action,
            message: 'Menu updated successfully',
            timestamp: new Date()
        });
    });
}

module.exports = { registerVendorHandlers };