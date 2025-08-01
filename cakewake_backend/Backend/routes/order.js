const express = require('express');
const router = express.Router();
const orderController = require('../controllers/orderController');

// Routes called from Socket.IO server (no auth middleware needed)
router.post('/vendor-accept', orderController.vendorAcceptOrder);
router.post('/delivery-accept', orderController.deliveryPartnerAccept);
router.post('/complete-delivery', orderController.completeDelivery);

module.exports = router;