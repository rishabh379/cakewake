package com.pvsrishabh.cakewake.features.cake_customization.presentation

import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeCustomization
import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeLayer
import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeViewMode

data class CakeCustomizationUiState(
    val cake: CakeCustomization,
    val availableBases: List<CakeLayer> = emptyList(),
    val availableSides: List<CakeLayer> = emptyList(),
    val availableTops: List<CakeLayer> = emptyList(),
    val availableIngredients: List<CakeLayer> = emptyList(),
    val availableTags: List<String> = emptyList(),
    val selectedLayerId: String? = null,
    val selectedTextId: String? = null,
    val selectedTab: CakeCustomizationTab = CakeCustomizationTab.BASE,
    val viewMode: CakeViewMode = CakeViewMode.TOP,
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val isLoading: Boolean = false,
    val selectedBaseCategory: String? = null, // <-- Add
    val selectedSideCategory: String? = null, // <-- Add
    val selectedTopCategory: String? = null,  // <-- Add
    val baseCategories: List<String> = emptyList(), // <-- Add
    val sideCategories: List<String> = emptyList(), // <-- Add
    val topCategories: List<String> = emptyList()
)