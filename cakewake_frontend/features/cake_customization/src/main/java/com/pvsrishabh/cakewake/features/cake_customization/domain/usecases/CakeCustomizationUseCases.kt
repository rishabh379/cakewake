package com.pvsrishabh.cakewake.features.cake_customization.domain.usecases

data class CakeCustomizationUseCases(
    val saveCakeCustomization: SaveCakeCustomization,
    val loadCakeCustomization: LoadCakeCustomization,
    val getAllCakeCustomizations: GetAllCakeCustomizations,
    val deleteCakeCustomization: DeleteCakeCustomization
)