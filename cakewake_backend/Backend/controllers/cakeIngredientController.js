const CakeIngredient = require('../models/Cake/cakeIngredient');

// Create a new ingredient
exports.createIngredient = async (req, res) => {
    try {
        const { name, category, price } = req.body;

        const ingredient = await CakeIngredient.create({
            name,
            category,
            price
        });

        res.status(201).json({ message: 'Ingredient created successfully', ingredient });
    } catch (error) {
        console.error('Error creating ingredient:', error);
        res.status(500).json({ message: 'Failed to create ingredient', error: error.message });
    }
};

// Get all ingredients
exports.getAllIngredients = async (req, res) => {
    try {
        const ingredients = await CakeIngredient.find();
        res.status(200).json({ ingredients });
    } catch (error) {
        res.status(500).json({ message: 'Failed to fetch ingredients', error: error.message });
    }
};

// Get ingredients by category
exports.getIngredientsByCategory = async (req, res) => {
    try {
        const { category } = req.params;
        const ingredients = await CakeIngredient.find({ 
            category: category.toUpperCase()
        });
        res.status(200).json({ ingredients });
    } catch (error) {
        res.status(500).json({ message: 'Failed to fetch ingredients by category', error: error.message });
    }
};

// Get a single ingredient by ID
exports.getIngredientById = async (req, res) => {
    try {
        const { id } = req.params;
        const ingredient = await CakeIngredient.findById(id);
        if (!ingredient) {
            return res.status(404).json({ message: 'Ingredient not found' });
        }
        res.status(200).json({ ingredient });
    } catch (error) {
        res.status(500).json({ message: 'Failed to fetch ingredient', error: error.message });
    }
};

// Update an ingredient
exports.updateIngredient = async (req, res) => {
    try {
        const { id } = req.params;
        const { name, category, price } = req.body;

        const updatedIngredient = await CakeIngredient.findByIdAndUpdate(
            id, 
            { name, category, price }, 
            { new: true }
        );
        
        if (!updatedIngredient) {
            return res.status(404).json({ message: 'Ingredient not found' });
        }
        
        res.status(200).json({ message: 'Ingredient updated successfully', ingredient: updatedIngredient });
    } catch (error) {
        res.status(500).json({ message: 'Failed to update ingredient', error: error.message });
    }
};

// Delete an ingredient
exports.deleteIngredient = async (req, res) => {
    try {
        const { id } = req.params;
        const ingredient = await CakeIngredient.findByIdAndDelete(id);
        
        if (!ingredient) {
            return res.status(404).json({ message: 'Ingredient not found' });
        }
        
        res.status(200).json({ message: 'Ingredient deleted successfully' });
    } catch (error) {
        res.status(500).json({ message: 'Failed to delete ingredient', error: error.message });
    }
};

// Seed ingredients from your Android constants
exports.seedIngredients = async (req, res) => {
    try {
        const ingredientsData = [
            // Base Layers
            { name: 'Sponge', category: 'BASE_LAYER', price: 50 },
            { name: 'Heart Shape', category: 'BASE_LAYER', price: 75 },
            { name: 'Chocolate Base', category: 'BASE_LAYER', price: 60 },
            { name: 'Vanilla Base', category: 'BASE_LAYER', price: 55 },
            { name: 'Red Velvet', category: 'BASE_LAYER', price: 80 },
            
            // Side Layers
            { name: 'Frosting', category: 'SIDE_LAYER', price: 40 },
            { name: 'Heart Cream', category: 'SIDE_LAYER', price: 45 },
            { name: 'Vanilla Cream', category: 'SIDE_LAYER', price: 35 },
            { name: 'Cream', category: 'SIDE_LAYER', price: 30 },
            { name: 'Fondant', category: 'SIDE_LAYER', price: 50 },
            { name: 'Buttercream', category: 'SIDE_LAYER', price: 45 },
            
            // Top Layers
            { name: 'Berries', category: 'TOP_LAYER', price: 60 },
            { name: 'Cherries', category: 'TOP_LAYER', price: 65 },
            { name: 'Chocolate Top', category: 'TOP_LAYER', price: 55 },
            
            // Ingredients/Toppings
            { name: 'Strawberry', category: 'INGREDIENT', price: 25 },
            { name: 'Dewberry', category: 'INGREDIENT', price: 30 },
            { name: 'Chocolate Chips', category: 'INGREDIENT', price: 20 },
            { name: 'Sprinkles', category: 'INGREDIENT', price: 15 },
            { name: 'Nuts', category: 'INGREDIENT', price: 35 }
        ];

        // Clear existing ingredients
        await CakeIngredient.deleteMany({});
        
        // Insert new ingredients
        const ingredients = await CakeIngredient.insertMany(ingredientsData);
        
        res.status(201).json({ 
            message: `${ingredients.length} ingredients seeded successfully`, 
            ingredients 
        });
    } catch (error) {
        console.error('Error seeding ingredients:', error);
        res.status(500).json({ message: 'Failed to seed ingredients', error: error.message });
    }
};