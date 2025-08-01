const jwt = require('jsonwebtoken');
const User = require('../models/user/User');
const Profile = require('../models/profile/profile');
const { deleteFromCloudinary } = require('./clodinaryController'); // adjust path if needed
const mongoose = require("mongoose");
const OTPSchema = require("../models/otp/OTPSchema");
const Location = require('../models/profile/location');
require('dotenv').config();

// Register the model if not already registered
const MobileOTP = mongoose.models.MobileOTP || mongoose.model("MobileOTP", OTPSchema);

// Generate OTP
function generateOTP() {
    return Math.floor(100000 + Math.random() * 900000).toString(); // 6-digit OTP
}

// Generate Token
function generateToken(user) {
    return jwt.sign(
        { id: user._id, role: user.role },
        process.env.JWT_SECRET,
        { expiresIn: '24h' } // Token expires in 24 hours
    );
}

// Unified Signup/Login Route
exports.signupOrLogin = async (req, res) => {
    const { mobileNumber } = req.body;

    if (!mobileNumber || !/^[0-9]{10}$/.test(mobileNumber)) {
        return res.status(400).json({ message: 'Invalid mobile number' });
    }

    try {
        // Check if the user exists
        const user = await User.findOne({ mobileNumber });

        // Generate OTP
        const otp = generateOTP();

        // Save OTP to the database
        await MobileOTP.create({ mobileNumber, otp });

        if (user) {
            // Login process
            return res.status(200).json({
                message: 'Login OTP sent successfully',
                isSignup: false, // Indicates this is a login process
            });
        } else {
            // Signup process
            return res.status(200).json({
                message: 'Signup OTP sent successfully',
                isSignup: true, // Indicates this is a signup process
            });
        }
    } catch (error) {
        console.error('Error in signupOrLogin:', error);
        return res.status(500).json({ message: 'Failed to process request' });
    }
};

// Verify OTP for Signup/Login
exports.verifyOTP = async (req, res) => {
    const { mobileNumber, otp } = req.body;

    if (!mobileNumber || !otp) {
        return res.status(400).json({ message: 'Mobile number and OTP are required' });
    }

    try {
        // Find the most recent OTP for this mobile number
        const recentOtp = await MobileOTP.find({ mobileNumber })
            .sort({ createdAt: -1 })
            .limit(1);

        if (!recentOtp.length || otp !== recentOtp[0].otp) {
            return res.status(400).json({
                success: false,
                message: "Invalid OTP",
            });
        }

        // OTP is valid, delete all OTPs for this number
        await MobileOTP.deleteMany({ mobileNumber });

        // Check if the user exists
        let user = await User.findOne({ mobileNumber });
        let isSignup = false;

        if (!user) {
            // Signup process: Create a new user and profile
            const profile = await Profile.create({
                name: 'Anonymous',
                image: null,
            });

            user = await User.create({
                mobileNumber,
                isVerified: true,
                profile: profile._id,
            });
            isSignup = true;
        } else {
            // Login process: Update verification status
            user.isVerified = true;
            await user.save();
        }

        // Generate token
        const token = generateToken(user);

        // Set token in cookies
        res.cookie('token', token, {
            httpOnly: true,
            secure: process.env.NODE_ENV === 'production',
            maxAge: 5 * 60 * 60 * 1000, // 5 hours
        });

        return res.status(200).json({
            message: isSignup ? 'Signup successful' : 'Login successful',
            token,
            user,
        });
    } catch (error) {
        console.error('Error verifying OTP:', error);
        return res.status(500).json({ message: 'Failed to verify OTP' });
    }
};

exports.getUserDetails = async (req, res) => {
    try {
        const userId = req.user.id; // Extract user ID from the token (set by auth middleware)

        // Find the user and populate the connected profile
        const user = await User.findById(userId).populate('profile');
        if (!user) {
            return res.status(404).json({ success: false, message: 'User not found' });
        }

        return res.status(200).json({ success: true, user });
    } catch (error) {
        console.error('Error fetching user details:', error);
        return res.status(500).json({ success: false, message: 'Failed to fetch user details' });
    }
};


exports.deleteUserAndProfile = async (req, res) => {
    try {
        const { id } = req.params;

        // Find the user
        const user = await User.findById(id);
        if (!user) {
            return res.status(404).json({ message: 'User not found' });
        }

        // Find and delete the profile
        const profile = await Profile.findById(user.profile);
        if (profile) {
            // Delete profile image from Cloudinary if exists
            if (profile.image) {
                // Extract public_id from the image URL
                // Example: https://res.cloudinary.com/<cloud_name>/image/upload/v1234567890/CakeWake/User_profile/abc123.jpg
                const urlParts = profile.image.split('/');
                // Find the index of 'upload' to get the path after it
                const uploadIndex = urlParts.findIndex(part => part === 'upload');
                const publicIdWithExtension = urlParts.slice(uploadIndex + 1).join('/'); // e.g., CakeWake/User_profile/abc123.jpg
                const publicId = publicIdWithExtension.replace(/\.[^/.]+$/, ""); // remove extension
                await deleteFromCloudinary(publicId);
            }

            // Delete all locations associated with this profile
            await Location.deleteMany({ profile: profile._id });

            // Delete the profile itself
            await Profile.findByIdAndDelete(profile._id);
        }

        // Delete the user
        await User.findByIdAndDelete(id);

        return res.status(200).json({ message: 'User, profile, locations, and profile image deleted successfully' });
    } catch (error) {
        console.error('Error deleting user:', error);
        return res.status(500).json({ message: 'Failed to delete user', error: error.message });
    }
};