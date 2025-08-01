const mongoose = require('mongoose');
const Location = require('./location'); // Import the Location model

const profileSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
    },
    image: {
        type: String,
    },
    locations: [
        {
            type: mongoose.Schema.Types.ObjectId,
            ref: 'Location', // Reference to the Location model
        },
    ],
    orders: [
        {
            type: mongoose.Schema.Types.ObjectId,
            ref: 'Order', // Reference to the Order model
        },
    ],
}, {
    timestamps: true,
});

module.exports = mongoose.model('Profile', profileSchema);