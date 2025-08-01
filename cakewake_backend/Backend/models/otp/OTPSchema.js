const mongoose = require("mongoose");

const OTPSchema = new mongoose.Schema({
    mobileNumber: {
        type: String,
        required: true,
        match: /^[0-9]{10}$/,
    },
    otp: {
        type: String,
        required: true,
    },
    createdAt: {
        type: Date,
        default: Date.now,
        expires: 60 * 5,
    },
});

module.exports = OTPSchema;