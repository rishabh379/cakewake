const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
    mobileNumber: {
        type: String,
        required: true,
        unique: true,
        match: /^[0-9]{10}$/, // Ensures a 10-digit mobile number
    },
    isVerified: {
        type: Boolean,
        default: false, // Indicates whether the mobile number is verified
    },
    profile: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Profile', // Reference to the Profile model
    }, 
    role: {
        type: String,
        enum: ['user', 'vendor', 'admin'], // Allowed roles
        default: 'user', // Default role is 'user'
    },
}, {
    timestamps: true, // Adds createdAt and updatedAt fields
});

module.exports = mongoose.models.User || mongoose.model('User', userSchema);