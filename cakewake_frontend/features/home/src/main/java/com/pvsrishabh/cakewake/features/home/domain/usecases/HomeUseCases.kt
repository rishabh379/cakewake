package com.pvsrishabh.cakewake.features.home.domain.usecases

data class HomeUseCases(
    val getCakes: GetCakesUseCase,
    val getCategories: GetCategoriesUseCase,
    val getSpecialOffers: GetSpecialOffersUseCase
)