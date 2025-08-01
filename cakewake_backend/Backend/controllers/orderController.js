const Order = require('../models/checkout/Order');

class OrderController {
    // Called when vendor accepts order via Socket.IO
    async vendorAcceptOrder(req, res) {
        try {
            const { orderId, vendorId, vendorName } = req.body;

            if (!orderId || !vendorId) {
                return res.status(400).json({
                    success: false,
                    message: 'Order ID and Vendor ID are required'
                });
            }

            const order = await Order.findById(orderId);
            if (!order) {
                return res.status(404).json({
                    success: false,
                    message: 'Order not found'
                });
            }

            // Update order with vendor assignment
            order.assignedVendor = {
                vendorId: vendorId,
                assignedAt: new Date()
            };
            order.status = 'VENDOR_ASSIGNED';
            
            await order.save();

            console.log(`✅ Order ${orderId} accepted by vendor ${vendorId} - DB updated`);

            res.status(200).json({
                success: true,
                message: 'Order assigned to vendor successfully',
                order: {
                    _id: order._id,
                    status: order.status,
                    assignedVendor: order.assignedVendor
                }
            });

        } catch (error) {
            console.error('Error accepting order:', error);
            res.status(500).json({
                success: false,
                message: 'Failed to accept order',
                error: error.message
            });
        }
    }

    // Called when delivery partner accepts delivery via Socket.IO
    async deliveryPartnerAccept(req, res) {
        try {
            const { orderId, deliveryPartnerId, deliveryPartnerName } = req.body;

            if (!orderId || !deliveryPartnerId) {
                return res.status(400).json({
                    success: false,
                    message: 'Order ID and Delivery Partner ID are required'
                });
            }

            const order = await Order.findById(orderId);
            if (!order) {
                return res.status(404).json({
                    success: false,
                    message: 'Order not found'
                });
            }

            if (order.assignedDeliveryPerson) {
                return res.status(400).json({
                    success: false,
                    message: 'Delivery partner already assigned to this order'
                });
            }

            // Assign delivery partner
            order.assignedDeliveryPerson = {
                deliveryPersonId: deliveryPartnerId,
                assignedAt: new Date()
            };
            order.status = 'DELIVERY_ASSIGNED';

            await order.save();

            console.log(`🚚 Delivery partner ${deliveryPartnerId} assigned to order ${orderId} - DB updated`);

            res.status(200).json({
                success: true,
                message: 'Delivery partner assigned successfully',
                order: {
                    _id: order._id,
                    status: order.status,
                    assignedDeliveryPerson: order.assignedDeliveryPerson
                }
            });

        } catch (error) {
            console.error('Error assigning delivery partner:', error);
            res.status(500).json({
                success: false,
                message: 'Failed to assign delivery partner',
                error: error.message
            });
        }
    }

    // Called when delivery is completed via Socket.IO
    async completeDelivery(req, res) {
        try {
            const { orderId, deliveryProof } = req.body;

            if (!orderId) {
                return res.status(400).json({
                    success: false,
                    message: 'Order ID is required'
                });
            }

            const order = await Order.findById(orderId);
            if (!order) {
                return res.status(404).json({
                    success: false,
                    message: 'Order not found'
                });
            }

            if (order.status === 'COMPLETED') {
                return res.status(400).json({
                    success: false,
                    message: 'Order is already completed'
                });
            }

            // Mark order as completed
            order.status = 'COMPLETED';
            order.completedAt = new Date();

            // Add delivery proof if provided
            if (deliveryProof) {
                order.deliveryProof = deliveryProof;
            }

            await order.save();

            console.log(`✅ Order ${orderId} marked as completed - DB updated`);

            res.status(200).json({
                success: true,
                message: 'Order delivery completed successfully',
                order: {
                    _id: order._id,
                    status: order.status,
                    completedAt: order.completedAt,
                    deliveryProof: deliveryProof
                }
            });

        } catch (error) {
            console.error('Error completing delivery:', error);
            res.status(500).json({
                success: false,
                message: 'Failed to complete delivery',
                error: error.message
            });
        }
    }
}

module.exports = new OrderController();