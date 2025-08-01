const Razorpay = require('razorpay');
const crypto = require('crypto');

class PaymentService {
    constructor() {
        this.razorpay = new Razorpay({
            key_id: process.env.RAZORPAY_KEY_ID,
            key_secret: process.env.RAZORPAY_KEY_SECRET,
        });
    }

    // Create payment order
    async createPaymentOrder(amount, orderId) {
        try {
            const options = {
                amount: amount * 100, // Amount in paise
                currency: 'INR',
                receipt: `order_${orderId}`,
                payment_capture: 1,
            };

            const paymentOrder = await this.razorpay.orders.create(options);
            return paymentOrder;
        } catch (error) {
            console.error('Error creating payment order:', error);
            throw error;
        }
    }

    // Verify payment signature
    verifyPaymentSignature(orderId, paymentId, signature) {
        try {
            const body = orderId + '|' + paymentId;
            const expectedSignature = crypto
                .createHmac('sha256', process.env.RAZORPAY_KEY_SECRET)
                .update(body.toString())
                .digest('hex');

            return expectedSignature === signature;
        } catch (error) {
            console.error('Error verifying payment signature:', error);
            return false;
        }
    }

    // Refund payment
    async refundPayment(paymentId, amount) {
        try {
            const refund = await this.razorpay.payments.refund(paymentId, {
                amount: amount * 100, // Amount in paise
            });
            return refund;
        } catch (error) {
            console.error('Error processing refund:', error);
            throw error;
        }
    }
}

module.exports = new PaymentService();