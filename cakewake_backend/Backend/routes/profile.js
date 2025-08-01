const express = require('express');
const router = express.Router();
const { auth, isUser } = require('../middleware/auth');
const {
    createProfile,
    getProfile,
    updateProfile,
    deleteProfile,
} = require('../controllers/profile');

// Route to get a profile by ID (only authenticated users)
router.get('/:id', auth, isUser, getProfile);

// Route to update a profile by ID (only authenticated users)
router.put('/:id', auth, isUser, updateProfile);

// Route to delete a profile by ID (only authenticated users)
router.delete('/:id', auth, isUser, deleteProfile);

module.exports = router;