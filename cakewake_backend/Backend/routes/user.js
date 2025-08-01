const express = require('express');
const router = express.Router();
const { signupOrLogin, verifyOTP, getUserDetails, deleteUserAndProfile } = require('../controllers/Auth');
const { auth } = require('../middleware/auth');

// Unified route for signup and login 
router.post('/signup-or-login', signupOrLogin);

// Route to verify OTP for signup or login
router.post('/verify-otp', verifyOTP);

// Protected route to get user details
router.get('/user', auth, getUserDetails);

// Protected route to delete user and profile
router.delete('/user/:id', auth, deleteUserAndProfile);

module.exports = router;