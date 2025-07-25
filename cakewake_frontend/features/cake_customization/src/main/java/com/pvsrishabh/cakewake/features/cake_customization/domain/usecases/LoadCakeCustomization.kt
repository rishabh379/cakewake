package com.pvsrishabh.cakewake.features.cake_customization.domain.usecases

import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeCustomization
import com.pvsrishabh.cakewake.features.cake_customization.domain.repository.CakeRepository

class LoadCakeCustomization(
    private val repository: CakeRepository
) {
    suspend operator fun invoke(cakeId: String): CakeCustomization {
        // Load cake, throwing an exception if not found
        return repository.loadCakeCustomization(cakeId)
    }
}