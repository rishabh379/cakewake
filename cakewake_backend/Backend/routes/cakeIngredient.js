const express = require('express');
const router = express.Router();
const cakeIngredientController = require('../controllers/cakeIngredientController');
const { auth } = require('../middleware/auth');

// All routes require authentication
router.get('/', auth, cakeIngredientController.getAllIngredients);
router.get('/category/:category', auth, cakeIngredientController.getIngredientsByCategory);
router.get('/:id', auth, cakeIngredientController.getIngredientById);
router.post('/', auth, cakeIngredientController.createIngredient);
router.put('/:id', auth, cakeIngredientController.updateIngredient);
router.delete('/:id', auth, cakeIngredientController.deleteIngredient);

// Seed route (run once to populate database)
router.post('/seed', auth, cakeIngredientController.seedIngredients);

module.exports = router;