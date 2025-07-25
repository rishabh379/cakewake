package com.pvsrishabh.cakewake.features.cake_customization.data.repository

import com.pvsrishabh.cakewake.features.cake_customization.data.local.database.CakeDao
import com.pvsrishabh.cakewake.features.cake_customization.data.local.database.CakeEntity
import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeCustomization
import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeViewMode
import com.pvsrishabh.cakewake.features.cake_customization.domain.repository.CakeRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CakeRepositoryImpl @Inject constructor(
    private val cakeDao: CakeDao
) : CakeRepository
{
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    override suspend fun saveCakeCustomization(cakeCustomization: CakeCustomization): String {
        val cakeEntity = mapToEntity(cakeCustomization)
        cakeDao.insertCake(cakeEntity)
        return cakeCustomization.id
    }

    override suspend fun loadCakeCustomization(cakeId: String): CakeCustomization {
        val cakeEntity = cakeDao.getCakeById(cakeId) ?: throw IllegalArgumentException("Cake with id $cakeId not found")
        return mapToDomain(cakeEntity)
    }

    override suspend fun deleteCakeCustomization(cakeId: String) {
        cakeDao.deleteCakeById(cakeId)
    }

    override suspend fun getAllCakeCustomizations(): List<CakeCustomization> {
        return cakeDao.getAllCakes().map { mapToDomain(it) }
    }

    override suspend fun searchCakeCustomizations(query: String): List<CakeCustomization> {
        return cakeDao.searchCakes(query).map { mapToDomain(it) }
    }

    override suspend fun getRecentCakeCustomizations(limit: Int): List<CakeCustomization> {
        return cakeDao.getRecentCakes(limit).map { mapToDomain(it) }
    }

    private fun mapToEntity(cakeCustomization: CakeCustomization): CakeEntity {
        return CakeEntity(
            id = cakeCustomization.id,
            createdAt = cakeCustomization.createdAt,
            viewMode = cakeCustomization.viewMode,
            baseLayer = cakeCustomization.baseLayer,
            sideLayer = cakeCustomization.sideLayer,
            topLayer = cakeCustomization.topLayer,
            ingredients = cakeCustomization.ingredients,
            textElements = cakeCustomization.textElements,
        )
    }

    private fun mapToDomain(cakeEntity: CakeEntity): CakeCustomization {
        return CakeCustomization(
            id = cakeEntity.id,
            createdAt = cakeEntity.createdAt,
            viewMode = cakeEntity.viewMode,
            baseLayer = cakeEntity.baseLayer,
            sideLayer = cakeEntity.sideLayer,
            topLayer = cakeEntity.topLayer,
            ingredients = cakeEntity.ingredients,
            textElements = cakeEntity.textElements
        )
    }
}

