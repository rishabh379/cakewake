package com.pvsrishabh.cakewake.features.cake_customization.domain.usecases

import com.pvsrishabh.cakewake.features.cake_customization.domain.repository.CakeRepository

class DeleteCakeCustomization (
    private val repository: CakeRepository
) {
    suspend operator fun invoke(cakeId: String) {
        repository.deleteCakeCustomization(cakeId)
    }
}