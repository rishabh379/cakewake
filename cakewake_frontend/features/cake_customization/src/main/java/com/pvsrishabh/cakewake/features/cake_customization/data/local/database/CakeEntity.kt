package com.pvsrishabh.cakewake.features.cake_customization.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeLayer
import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeText
import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeViewMode

@Entity(tableName = "cake_customizations")
@TypeConverters(Converters::class)
data class CakeEntity(
    @PrimaryKey val id: String,
    val createdAt: Long,
    val viewMode: CakeViewMode,
    val baseLayer: CakeLayer,
    val sideLayer: CakeLayer,
    val topLayer: CakeLayer,
    val ingredients: List<CakeLayer> = emptyList(),
    val textElements: List<CakeText> = emptyList()
)
