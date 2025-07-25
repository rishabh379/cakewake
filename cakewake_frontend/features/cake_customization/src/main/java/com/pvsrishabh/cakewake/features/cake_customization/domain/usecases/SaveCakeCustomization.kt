package com.pvsrishabh.cakewake.features.cake_customization.domain.usecases

import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeCustomization
import com.pvsrishabh.cakewake.features.cake_customization.domain.repository.CakeRepository

class SaveCakeCustomization(
    private val repository: CakeRepository
) {
    suspend operator fun invoke(cakeCustomization: CakeCustomization): String {
        return repository.saveCakeCustomization(cakeCustomization)
    }
}