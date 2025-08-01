const mongoose = require('mongoose');

const cakeIngredientSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
        unique: true
    },
    category: {
        type: String,
        required: true,
        enum: ['BASE_LAYER', 'SIDE_LAYER', 'TOP_LAYER', 'INGREDIENT']
    },
    price: {
        type: Number,
        required: true,
        min: 0
    }
}, {
    timestamps: true,
});

module.exports = mongoose.model('CakeIngredient', cakeIngredientSchema);