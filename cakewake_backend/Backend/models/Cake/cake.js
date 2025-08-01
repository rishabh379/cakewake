const mongoose = require('mongoose');

const cakeSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
    },
    photo: {
        type: String, // Cloudinary URL or local path
        required: true,
    },
    price: {
        type: Number,
        required: true,
    },
    sugarFree: {
        type: Boolean,
        default: false,
    },
    discount: {
        type: Number,
        default: 0,
    }
}, {
    timestamps: true,
});

module.exports = mongoose.model('Cake', cakeSchema);