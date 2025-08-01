const axios = require('axios');
const websocketManager = require('./websocketManager');

class VendorService {
    constructor() {
        this.vendorAppBaseUrl = process.env.VENDOR_APP_BASE_URL || 'https://cakewake-9huv.onrender.com/api/v1';
        console.log('🔧 VendorService initialized with URL:', this.vendorAppBaseUrl);
        this.setupWebSocketListeners();
    }

    setupWebSocketListeners() {
        // Listen for vendor responses
        websocketManager.on('vendorResponse', (data) => {
            this.handleVendorResponse(data);
        });

        // Listen for vendor availability updates
        websocketManager.on('vendorAvailability', (data) => {
            this.handleVendorAvailability(data);
        });
    }

    // Get available vendors using the actual API
    async getAvailableVendors(city, area) {
        try {
            console.log(`🔍 Fetching vendors for ${city}, ${area}`);
            
            const response = await axios.get(`${this.vendorAppBaseUrl}/public/search-vendors`, {
                params: { city, area },
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.data.success && response.data.vendors) {
                // Transform vendor data to match our order structure
                const transformedVendors = response.data.vendors.map(vendor => ({
                    vendorId: vendor._id,
                    name: vendor.profile.businessDetail.name,
                    businessName: vendor.profile.businessDetail.businessName,
                    phone: vendor.mobileNumber,
                    businessType: vendor.profile.businessDetail.businessType,
                    city: vendor.profile.deliverySetup.city,
                    workingAreas: vendor.profile.deliverySetup.workingArea,
                    isOnline: vendor.isOnline,
                    isVerified: vendor.isVerified,
                    lastOnline: vendor.lastOnline
                }));

                console.log(`✅ Found ${transformedVendors.length} vendors`);
                return transformedVendors;
            } else {
                console.log('❌ No vendors found or API error');
                return [];
            }
        } catch (error) {
            console.error('Error fetching vendors:', error.message);
            return [];
        }
    }

    // Broadcast order to vendors (via WebSocket)
    async broadcastOrderToVendors(orderData, vendorIds) {
        try {
            if (websocketManager.isConnected('vendor')) {
                const broadcastData = {
                    ...orderData,
                    targetVendors: vendorIds,
                    broadcastType: 'ORDER_BROADCAST',
                    timeout: 300000 // 5 minutes
                };
                
                const success = websocketManager.broadcastOrderToVendors(broadcastData);
                console.log(`📢 Order ${orderData.orderId} broadcasted to ${vendorIds.length} vendors`);
                return success;
            } else {
                console.warn('⚠️ WebSocket connection to vendor app not available');
                return false;
            }
        } catch (error) {
            console.error('Error broadcasting order:', error);
            throw error;
        }
    }

    // Assign order to specific vendor (via WebSocket)
    async assignOrderToVendor(orderId, vendorId) {
        try {
            if (websocketManager.isConnected('vendor')) {
                const success = websocketManager.assignOrderToVendor(orderId, vendorId);
                console.log(`🎯 Order ${orderId} assigned to vendor ${vendorId}`);
                return success;
            } else {
                console.warn('⚠️ WebSocket connection to vendor app not available');
                return false;
            }
        } catch (error) {
            console.error('Error assigning order to vendor:', error);
            throw error;
        }
    }

    // Handle vendor response to order broadcast
    handleVendorResponse(data) {
        console.log('📨 Vendor response received:', data);
        // Update order in database
        const Order = require('../models/checkout/Order');
        Order.findByIdAndUpdate(data.orderId, {
            $push: {
                vendorResponses: {
                    vendorId: data.vendorId,
                    respondedAt: new Date(),
                    accepted: data.accepted
                }
            },
            ...(data.accepted && !data.selectedVendor ? {
                selectedVendor: data.vendorData,
                status: 'PAYMENT_PENDING'
            } : {})
        }).exec();
    }

    // Handle vendor availability updates
    handleVendorAvailability(data) {
        console.log('📍 Vendor availability updated:', data);
    }
}

module.exports = new VendorService();