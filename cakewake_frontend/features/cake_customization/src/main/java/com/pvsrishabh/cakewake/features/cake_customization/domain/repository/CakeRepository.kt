package com.pvsrishabh.cakewake.features.cake_customization.domain.repository

import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeCustomization

interface CakeRepository {
    suspend fun saveCakeCustomization(cakeCustomization: CakeCustomization): String
    suspend fun loadCakeCustomization(cakeId: String): CakeCustomization
    suspend fun deleteCakeCustomization(cakeId: String)
    suspend fun getAllCakeCustomizations(): List<CakeCustomization>
    suspend fun searchCakeCustomizations(query: String): List<CakeCustomization>
    suspend fun getRecentCakeCustomizations(limit: Int): List<CakeCustomization>
}