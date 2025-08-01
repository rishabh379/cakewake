const express = require('express');
const router = express.Router();
const cakeController = require('../controllers/cakeController');
const { auth } = require('../middleware/auth');

// Only authenticated users can access these routes
router.post('/', auth, cakeController.createCake);
router.get('/', auth, cakeController.getAllCakes);
router.get('/:id', auth, cakeController.getCakeById);
router.put('/:id', auth, cakeController.updateCake);
router.delete('/:id', auth, cakeController.deleteCake);

module.exports = router;