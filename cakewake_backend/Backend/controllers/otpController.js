const mongoose = require("mongoose");
const OTPSchema = require("../models/otp/OTPSchema");
const { sendOTPViaSMS } = require("../config/twilio");

// Attach pre-save hook to the schema
OTPSchema.pre("save", async function (next) {
    console.log("New OTP document saved to database");
    if (this.isNew) {
        await sendOTPViaSMS(this.mobileNumber, this.otp);
    }
    next();
});

// Register the model if not already registered
const MobileOTP = mongoose.models.MobileOTP || mongoose.model("MobileOTP", OTPSchema);

// Controller to create and send OTP
exports.createAndSendOTP = async (req, res) => {
    const { mobileNumber, otp } = req.body;
    try {
        const otpDoc = new MobileOTP({ mobileNumber, otp });
        await otpDoc.save(); // Pre-save hook will send the OTP
        res.status(201).json({ message: "OTP sent successfully" });
    } catch (error) {
        res.status(500).json({ error: "Failed to send OTP" });
    }
};

module.exports = {
    createAndSendOTP,
    MobileOTP,
};