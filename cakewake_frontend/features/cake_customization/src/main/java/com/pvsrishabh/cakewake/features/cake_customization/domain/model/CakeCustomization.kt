package com.pvsrishabh.cakewake.features.cake_customization.domain.model

import com.android.identity.util.UUID

data class CakeCustomization(
    val id: String = UUID.randomUUID().toString(),
    val createdAt: Long = System.currentTimeMillis(),
    val viewMode: CakeViewMode = CakeViewMode.TOP,
    val baseLayer: CakeLayer,
    val sideLayer: CakeLayer,
    val topLayer: CakeLayer,
    val ingredients: List<CakeLayer> = emptyList(),
    val textElements: List<CakeText> = emptyList()
)

enum class CakeViewMode {
    TOP, SIDE
}