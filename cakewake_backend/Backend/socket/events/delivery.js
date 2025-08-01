function registerDeliveryHandlers(socket) {
    console.log(`🚚 Registering delivery handlers for partner ${socket.user.id}`);

    // 🏠 Event: Delivery partner joins work area
    socket.on("join-work-area", ({ workArea, workCity }) => {
        const areaRoom = `delivery_partner_${workCity}_${workArea}`;
        const personalRoom = `delivery_partner_${socket.user.id}`;
      
        socket.join(areaRoom);       // for receiving delivery requests
        socket.join(personalRoom);   // for direct updates
      
        console.log(`🚚 Delivery partner ${socket.user.id} joined area room: ${areaRoom}`);
        console.log(`🏠 Delivery partner ${socket.user.id} joined personal room: ${personalRoom}`);
        
        socket.emit('work-area-joined', {
            workArea,
            workCity,
            areaRoom,
            personalRoom,
            timestamp: new Date()
        });
    });

    // ✅ Event: Delivery partner accepts delivery request
    socket.on("accept-delivery", ({ orderId, vendorId, userId, orderDetails }) => {
        const vendorRoom = `vendor_${vendorId}`;
        const userRoom = `user_${userId}`;
        
        console.log(`✅ Delivery partner ${socket.user.id} accepting delivery for order ${orderId}`);
        
        // Notify vendor
        socket.to(vendorRoom).emit("delivery-accepted-by-partner", {
            orderId,
            deliveryPartnerId: socket.user.id,
            deliveryPartnerName: socket.user.profile?.name || 'Delivery Partner',
            deliveryPartnerPhone: socket.user.mobileNumber,
            vehicleInfo: socket.user.profile?.vehicleDetail || {},
            orderDetails,
            message: 'Delivery partner assigned to your order',
            timestamp: new Date()
        });

        // Notify user
        socket.to(userRoom).emit("delivery-partner-assigned", {
            orderId,
            deliveryPartnerId: socket.user.id,
            deliveryPartnerName: socket.user.profile?.name || 'Delivery Partner',
            deliveryPartnerPhone: socket.user.mobileNumber,
            vehicleInfo: socket.user.profile?.vehicleDetail || {},
            orderDetails,
            message: 'Delivery partner has been assigned to your order',
            timestamp: new Date()
        });

        // Confirm to delivery partner
        socket.emit('delivery-accepted-confirmed', {
            orderId,
            vendorId,
            userId,
            message: 'Delivery request accepted successfully',
            timestamp: new Date()
        });

        console.log(`📤 Delivery acceptance notifications sent`);
    });

    // 🚫 Event: Delivery partner rejects delivery request
    socket.on("reject-delivery", ({ orderId, vendorId, reason }) => {
        const vendorRoom = `vendor_${vendorId}`;
        
        console.log(`🚫 Delivery partner ${socket.user.id} rejecting delivery for order ${orderId}: ${reason}`);
        
        // Notify vendor
        socket.to(vendorRoom).emit("delivery-rejected-by-partner", {
            orderId,
            deliveryPartnerId: socket.user.id,
            deliveryPartnerName: socket.user.profile?.name || 'Delivery Partner',
            reason,
            message: 'Delivery partner declined the request',
            timestamp: new Date()
        });

        // Confirm to delivery partner
        socket.emit('delivery-rejected-confirmed', {
            orderId,
            reason,
            timestamp: new Date()
        });
    });

    // 📍 Event: Delivery partner updates location
    socket.on("update-location", ({ orderId, location, userId, vendorId }) => {
        const userRoom = `user_${userId}`;
        const orderRoom = `order_${orderId}`;
        const orderTrackingRoom = `order_tracking_${orderId}`;
        
        console.log(`📍 Delivery partner ${socket.user.id} updating location for order ${orderId}`);
        
        // Send location to user and order subscribers
        socket.to(userRoom).emit("delivery-location-update", {
            orderId,
            deliveryPartnerId: socket.user.id,
            location: {
                latitude: location.latitude,
                longitude: location.longitude,
                accuracy: location.accuracy || null,
                heading: location.heading || null,
                speed: location.speed || null,
                altitude: location.altitude || null
            },
            timestamp: new Date()
        });

        socket.to(orderRoom).emit("delivery-location-update", {
            orderId,
            deliveryPartnerId: socket.user.id,
            location,
            timestamp: new Date()
        });

        socket.to(orderTrackingRoom).emit("delivery-location-update", {
            orderId,
            deliveryPartnerId: socket.user.id,
            location,
            timestamp: new Date()
        });

        // Optionally notify vendor about location updates
        if (vendorId) {
            socket.to(`vendor_${vendorId}`).emit("delivery-location-update", {
                orderId,
                deliveryPartnerId: socket.user.id,
                location,
                timestamp: new Date()
            });
        }

        // Confirm to delivery partner
        socket.emit('location-updated', {
            orderId,
            location,
            timestamp: new Date()
        });
    });

    // 🚚 Event: Delivery partner starts delivery
    socket.on("start-delivery", ({ orderId, vendorId, userId }) => {
        const vendorRoom = `vendor_${vendorId}`;
        const userRoom = `user_${userId}`;
        const orderRoom = `order_${orderId}`;
        
        console.log(`🚚 Delivery partner ${socket.user.id} starting delivery for order ${orderId}`);
        
        // Notify vendor
        socket.to(vendorRoom).emit("delivery-started", {
            orderId,
            deliveryPartnerId: socket.user.id,
            deliveryPartnerName: socket.user.profile?.name || 'Delivery Partner',
            message: 'Delivery has started',
            timestamp: new Date()
        });

        // Notify user
        socket.to(userRoom).emit("delivery-started", {
            orderId,
            deliveryPartnerId: socket.user.id,
            deliveryPartnerName: socket.user.profile?.name || 'Delivery Partner',
            message: 'Your order is out for delivery!',
            timestamp: new Date()
        });

        // Notify order subscribers
        socket.to(orderRoom).emit("delivery-started", {
            orderId,
            deliveryPartnerId: socket.user.id,
            timestamp: new Date()
        });

        // Confirm to delivery partner
        socket.emit('delivery-started-confirmed', {
            orderId,
            message: 'Delivery started successfully',
            timestamp: new Date()
        });
    });

    // 🛍️ Event: Delivery partner picks up order from vendor
    socket.on("pickup-order", ({ orderId, vendorId, userId }) => {
        const vendorRoom = `vendor_${vendorId}`;
        const userRoom = `user_${userId}`;
        const orderRoom = `order_${orderId}`;
        
        console.log(`🛍️ Delivery partner ${socket.user.id} picked up order ${orderId} from vendor`);
        
        // Notify vendor
        socket.to(vendorRoom).emit("order-picked-up", {
            orderId,
            deliveryPartnerId: socket.user.id,
            deliveryPartnerName: socket.user.profile?.name || 'Delivery Partner',
            message: 'Order has been picked up',
            timestamp: new Date()
        });

        // Notify user
        socket.to(userRoom).emit("order-picked-up", {
            orderId,
            deliveryPartnerId: socket.user.id,
            deliveryPartnerName: socket.user.profile?.name || 'Delivery Partner',
            message: 'Your order has been picked up and is on the way!',
            timestamp: new Date()
        });

        // Notify order subscribers
        socket.to(orderRoom).emit("order-picked-up", {
            orderId,
            deliveryPartnerId: socket.user.id,
            timestamp: new Date()
        });

        // Confirm to delivery partner
        socket.emit('pickup-confirmed', {
            orderId,
            message: 'Order pickup confirmed',
            timestamp: new Date()
        });
    });

    // ✅ Event: Delivery partner completes delivery
    socket.on("complete-delivery", ({ orderId, vendorId, userId, deliveryProof }) => {
        const vendorRoom = `vendor_${vendorId}`;
        const userRoom = `user_${userId}`;
        const orderRoom = `order_${orderId}`;
        
        console.log(`✅ Delivery partner ${socket.user.id} completing delivery for order ${orderId}`);
        
        // Notify vendor
        socket.to(vendorRoom).emit("delivery-completed-update", {
            orderId,
            deliveryPartnerId: socket.user.id,
            deliveryPartnerName: socket.user.profile?.name || 'Delivery Partner',
            deliveryProof: {
                photo: deliveryProof?.photo || null,
                signature: deliveryProof?.signature || null,
                notes: deliveryProof?.notes || null,
                recipientName: deliveryProof?.recipientName || null
            },
            message: 'Order delivery completed successfully',
            timestamp: new Date()
        });

        // Notify user
        socket.to(userRoom).emit("delivery-completed-update", {
            orderId,
            deliveryPartnerId: socket.user.id,
            deliveryPartnerName: socket.user.profile?.name || 'Delivery Partner',
            deliveryProof: {
                photo: deliveryProof?.photo || null,
                signature: deliveryProof?.signature || null,
                notes: deliveryProof?.notes || null,
                recipientName: deliveryProof?.recipientName || null
            },
            message: 'Your order has been delivered successfully!',
            timestamp: new Date()
        });

        // Notify order subscribers
        socket.to(orderRoom).emit("delivery-completed-update", {
            orderId,
            deliveryPartnerId: socket.user.id,
            deliveryProof,
            timestamp: new Date()
        });

        // Confirm to delivery partner
        socket.emit('delivery-completed-confirmed', {
            orderId,
            deliveryProof,
            message: 'Delivery marked as completed',
            timestamp: new Date()
        });

        console.log(`🎉 Delivery completion notifications sent for order ${orderId}`);
    });

    // 💬 Event: Delivery partner sends message to customer
    socket.on("message-to-customer", ({ orderId, userId, message }) => {
        const userRoom = `user_${userId}`;
        
        console.log(`💬 Delivery partner ${socket.user.id} sending message to user ${userId} for order ${orderId}`);
        
        socket.to(userRoom).emit("delivery-message", {
            orderId,
            deliveryPartnerId: socket.user.id,
            deliveryPartnerName: socket.user.profile?.name || 'Delivery Partner',
            message,
            timestamp: new Date()
        });

        // Confirm to delivery partner
        socket.emit('message-sent', {
            orderId,
            userId,
            message,
            timestamp: new Date()
        });
    });

    // 💬 Event: Delivery partner sends message to vendor
    socket.on("message-to-vendor", ({ orderId, vendorId, message }) => {
        const vendorRoom = `vendor_${vendorId}`;
        
        console.log(`💬 Delivery partner ${socket.user.id} sending message to vendor ${vendorId} for order ${orderId}`);
        
        socket.to(vendorRoom).emit("delivery-message", {
            orderId,
            deliveryPartnerId: socket.user.id,
            deliveryPartnerName: socket.user.profile?.name || 'Delivery Partner',
            message,
            timestamp: new Date()
        });

        // Confirm to delivery partner
        socket.emit('message-sent', {
            orderId,
            vendorId,
            message,
            timestamp: new Date()
        });
    });

    // 📞 Event: Delivery partner calls customer
    socket.on("call-customer", ({ orderId, userId }) => {
        const userRoom = `user_${userId}`;
        
        console.log(`📞 Delivery partner ${socket.user.id} calling user ${userId} for order ${orderId}`);
        
        socket.to(userRoom).emit("delivery-partner-calling", {
            orderId,
            deliveryPartnerId: socket.user.id,
            deliveryPartnerName: socket.user.profile?.name || 'Delivery Partner',
            deliveryPartnerPhone: socket.user.mobileNumber,
            timestamp: new Date()
        });

        // Confirm to delivery partner
        socket.emit('call-initiated', {
            orderId,
            userId,
            message: 'Call request sent to customer',
            timestamp: new Date()
        });
    });

    // 📞 Event: Delivery partner calls vendor
    socket.on("call-vendor", ({ orderId, vendorId }) => {
        const vendorRoom = `vendor_${vendorId}`;
        
        console.log(`📞 Delivery partner ${socket.user.id} calling vendor ${vendorId} for order ${orderId}`);
        
        socket.to(vendorRoom).emit("delivery-partner-calling", {
            orderId,
            deliveryPartnerId: socket.user.id,
            deliveryPartnerName: socket.user.profile?.name || 'Delivery Partner',
            deliveryPartnerPhone: socket.user.mobileNumber,
            timestamp: new Date()
        });

        // Confirm to delivery partner
        socket.emit('call-initiated', {
            orderId,
            vendorId,
            message: 'Call request sent to vendor',
            timestamp: new Date()
        });
    });

    // 🕒 Event: Delivery partner updates ETA
    socket.on("update-eta", ({ orderId, userId, vendorId, eta, message }) => {
        const userRoom = `user_${userId}`;
        const orderRoom = `order_${orderId}`;
        
        console.log(`🕒 Delivery partner ${socket.user.id} updating ETA for order ${orderId}`);
        
        // Notify user and order subscribers
        socket.to(userRoom).emit("delivery-eta-updated", {
            orderId,
            deliveryPartnerId: socket.user.id,
            deliveryPartnerName: socket.user.profile?.name || 'Delivery Partner',
            eta,
            message,
            timestamp: new Date()
        });

        socket.to(orderRoom).emit("delivery-eta-updated", {
            orderId,
            deliveryPartnerId: socket.user.id,
            eta,
            message,
            timestamp: new Date()
        });

        // Optionally notify vendor
        if (vendorId) {
            socket.to(`vendor_${vendorId}`).emit("delivery-eta-updated", {
                orderId,
                deliveryPartnerId: socket.user.id,
                eta,
                message,
                timestamp: new Date()
            });
        }

        // Confirm to delivery partner
        socket.emit('eta-updated-confirmed', {
            orderId,
            eta,
            message,
            timestamp: new Date()
        });
    });

    // 🚚 Event: Delivery partner updates availability status
    socket.on("update-availability", ({ isAvailable, location, reason }) => {
        console.log(`🚚 Delivery partner ${socket.user.id} updating availability: ${isAvailable ? 'Available' : 'Unavailable'}`);
        
        // Confirm to delivery partner
        socket.emit('availability-updated', {
            isAvailable,
            location,
            reason,
            message: `Status updated to ${isAvailable ? 'Available' : 'Unavailable'}`,
            timestamp: new Date()
        });
    });

    // 🚨 Event: Delivery partner reports issue
    socket.on("report-issue", ({ orderId, vendorId, userId, issue, category, severity }) => {
        console.log(`🚨 Delivery partner ${socket.user.id} reporting issue for order ${orderId}: ${issue}`);
        
        // Notify vendor
        if (vendorId) {
            socket.to(`vendor_${vendorId}`).emit("delivery-issue-reported", {
                orderId,
                deliveryPartnerId: socket.user.id,
                deliveryPartnerName: socket.user.profile?.name || 'Delivery Partner',
                issue,
                category,
                severity,
                timestamp: new Date()
            });
        }

        // Notify user
        if (userId) {
            socket.to(`user_${userId}`).emit("delivery-issue-reported", {
                orderId,
                deliveryPartnerId: socket.user.id,
                deliveryPartnerName: socket.user.profile?.name || 'Delivery Partner',
                issue,
                category,
                severity,
                message: 'There is an issue with your delivery. We are working to resolve it.',
                timestamp: new Date()
            });
        }

        // Confirm to delivery partner
        socket.emit('issue-reported-confirmed', {
            orderId,
            issue,
            category,
            severity,
            timestamp: new Date()
        });
    });

    // ❌ Event: Delivery partner cancels delivery
    socket.on("cancel-delivery", ({ orderId, vendorId, userId, reason }) => {
        console.log(`❌ Delivery partner ${socket.user.id} cancelling delivery for order ${orderId}: ${reason}`);
        
        // Notify vendor
        socket.to(`vendor_${vendorId}`).emit("delivery-cancelled-by-partner", {
            orderId,
            deliveryPartnerId: socket.user.id,
            deliveryPartnerName: socket.user.profile?.name || 'Delivery Partner',
            reason,
            message: 'Delivery has been cancelled by the delivery partner',
            timestamp: new Date()
        });

        // Notify customer
        socket.to(`user_${userId}`).emit("delivery-cancelled-by-partner", {
            orderId,
            deliveryPartnerId: socket.user.id,
            deliveryPartnerName: socket.user.profile?.name || 'Delivery Partner',
            reason,
            message: 'Your delivery has been cancelled. A new delivery partner will be assigned.',
            timestamp: new Date()
        });

        // Confirm to delivery partner
        socket.emit('delivery-cancel-confirmed', {
            orderId,
            reason,
            message: 'Delivery cancellation confirmed',
            timestamp: new Date()
        });
    });

    // 🆘 Event: Delivery partner needs emergency assistance
    socket.on("emergency-help", ({ orderId, emergency, location }) => {
        console.log(`🆘 Delivery partner ${socket.user.id} requesting emergency help for order ${orderId}`);
        
        // This could notify admin/support team and relevant parties
        socket.emit('emergency-acknowledged', {
            orderId,
            emergency,
            location,
            message: 'Emergency request received. Help is on the way.',
            timestamp: new Date()
        });
    });

    // 📊 Event: Delivery partner requests earnings/stats
    socket.on("request-earnings", ({ period }) => {
        console.log(`📊 Delivery partner ${socket.user.id} requesting earnings for period: ${period}`);
        
        // This would typically fetch earnings from database
        socket.emit('earnings-data', {
            period,
            message: 'Earnings data fetched',
            timestamp: new Date()
        });
    });

    // 🔋 Event: Delivery partner updates battery/vehicle status
    socket.on("update-vehicle-status", ({ batteryLevel, fuelLevel, vehicleIssues }) => {
        console.log(`🔋 Delivery partner ${socket.user.id} updating vehicle status`);
        
        // Confirm to delivery partner
        socket.emit('vehicle-status-updated', {
            batteryLevel,
            fuelLevel,
            vehicleIssues,
            message: 'Vehicle status updated',
            timestamp: new Date()
        });
    });
}

module.exports = { registerDeliveryHandlers };