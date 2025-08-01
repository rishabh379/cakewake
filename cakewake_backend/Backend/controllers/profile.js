const Profile = require('../models/profile/profile');
const { uploadProfilePic } = require('./clodinaryController');

// Get a profile by ID
exports.getProfile = async (req, res) => {
    const { id } = req.params;

    try {
        const profile = await Profile.findById(id);
        if (!profile) {
            return res.status(404).json({ message: 'Profile not found' });
        }

        return res.status(200).json(profile);
    } catch (error) {
        console.error('Error fetching profile:', error);
        return res.status(500).json({ message: 'Failed to fetch profile' });
    }
};

// Update a profile by ID
exports.updateProfile = async (req, res) => {
    const { id } = req.params;
    const { email, name } = req.body;

    try {
        let updateData = { name };
        // If a new profile picture is uploaded, upload to Cloudinary
        if (req.files && req.files.profilePicture) {
            const imageUrl = await uploadProfilePic(req.files.profilePicture);
            updateData.image = imageUrl;
        }
        if (email) updateData.email = email; // Only add email if provided

        const updatedProfile = await Profile.findByIdAndUpdate(
            id,
            { $set: updateData },
            { new: true }
        );

        if (!updatedProfile) {
            return res.status(404).json({ message: 'Profile not found' });
        }

        return res.status(200).json({ message: 'Profile updated successfully', profile: updatedProfile });
    } catch (error) {
        console.error('Error updating profile:', error);
        return res.status(500).json({ message: 'Failed to update profile' });
    }
};

// Delete a profile by ID
exports.deleteProfile = async (req, res) => {
    const { id } = req.params;

    try {
        const profile = await Profile.findByIdAndDelete(id);

        if (!profile) {
            return res.status(404).json({ message: 'Profile not found' });
        }

        return res.status(200).json({ message: 'Profile deleted successfully' });
    } catch (error) {
        console.error('Error deleting profile:', error);
        return res.status(500).json({ message: 'Failed to delete profile' });
    }
};