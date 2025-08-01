const Cake = require('../models/Cake/cake');
const { uploadCakeImage, deleteFromCloudinary } = require('./clodinaryController');

// Create a new cake
exports.createCake = async (req, res) => {
    try {
        const { name, price, sugarFree, discount } = req.body;
        let photoUrl = null;

        if (req.files && req.files.photo) {
            photoUrl = await uploadCakeImage(req.files.photo);
        } else {
            return res.status(400).json({ message: 'Cake photo is required' });
        }

        const cake = await Cake.create({
            name,
            photo: photoUrl,
            price,
            sugarFree,
            discount,
        });

        res.status(201).json({ message: 'Cake created successfully', cake });
    } catch (error) {
        console.error('Error creating cake:', error);
        res.status(500).json({ message: 'Failed to create cake', error: error.message });
    }
};

// Get all cakes
exports.getAllCakes = async (req, res) => {
    try {
        const cakes = await Cake.find();
        res.status(200).json({ cakes });
    } catch (error) {
        res.status(500).json({ message: 'Failed to fetch cakes', error: error.message });
    }
};

// Get a single cake by ID
exports.getCakeById = async (req, res) => {
    try {
        const { id } = req.params;
        const cake = await Cake.findById(id);
        if (!cake) {
            return res.status(404).json({ message: 'Cake not found' });
        }
        res.status(200).json({ cake });
    } catch (error) {
        res.status(500).json({ message: 'Failed to fetch cake', error: error.message });
    }
};

// Update a cake by ID
exports.updateCake = async (req, res) => {
    try {
        const { id } = req.params;
        const { name, price, sugarFree, discount } = req.body;
        let updateData = { name, price, sugarFree, discount };

        if (req.files && req.files.photo) {
            const photoUrl = await uploadCakeImage(req.files.photo);
            updateData.photo = photoUrl;
        }

        const updatedCake = await Cake.findByIdAndUpdate(id, { $set: updateData }, { new: true });
        if (!updatedCake) {
            return res.status(404).json({ message: 'Cake not found' });
        }
        res.status(200).json({ message: 'Cake updated successfully', cake: updatedCake });
    } catch (error) {
        res.status(500).json({ message: 'Failed to update cake', error: error.message });
    }
};

// Delete a cake by ID
exports.deleteCake = async (req, res) => {
    try {
        const { id } = req.params;
        const cake = await Cake.findByIdAndDelete(id);
        if (!cake) {
            return res.status(404).json({ message: 'Cake not found' });
        }
        // Delete cake image from Cloudinary
        if (cake.photo) {
            await deleteFromCloudinary(cake.photo);
        }
        res.status(200).json({ message: 'Cake and image deleted successfully' });
    } catch (error) {
        res.status(500).json({ message: 'Failed to delete cake', error: error.message });
    }
};