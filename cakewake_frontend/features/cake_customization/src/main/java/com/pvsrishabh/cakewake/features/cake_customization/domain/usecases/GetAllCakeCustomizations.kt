package com.pvsrishabh.cakewake.features.cake_customization.domain.usecases

import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeCustomization
import com.pvsrishabh.cakewake.features.cake_customization.domain.repository.CakeRepository

class GetAllCakeCustomizations(
    private val repository: CakeRepository
) {
    suspend operator fun invoke(): List<CakeCustomization> {
        return repository.getAllCakeCustomizations()
    }
}