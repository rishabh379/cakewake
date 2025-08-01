const express = require('express');
const router = express.Router();
const {
    handleVendorNotification,
    getNotificationStatus
} = require('../controllers/vendorNotificationController');

// Handle vendor notifications (POST)
router.post('/', handleVendorNotification);

// Get notification status for an order (GET) - optional utility endpoint
router.get('/:orderId', getNotificationStatus);

// Health check endpoint for vendor notifications
router.get('/health', (req, res) => {
    res.status(200).json({
        success: true,
        message: 'Vendor notification service is running',
        timestamp: new Date().toISOString()
    });
});

module.exports = router;