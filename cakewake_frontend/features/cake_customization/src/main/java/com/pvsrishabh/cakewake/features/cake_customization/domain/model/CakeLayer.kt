package com.pvsrishabh.cakewake.features.cake_customization.domain.model

import com.android.identity.util.UUID
import kotlinx.serialization.Serializable

// Data class to represent a cake layer
@Serializable
data class CakeLayer(
    val id: String = UUID.randomUUID().toString(),
    val type: CakeLayerType,
    val imageResourceId: Int,
    val position: SerializablePointF = SerializablePointF(0f, 0f),
    val rotation: Float = 0f,
    val scale: Float = 1f,
    val zIndex: Int = 0,
    val categories: List<String> = emptyList()
)

// Enum for cake layer types
@Serializable
enum class CakeLayerType {
    BASE, SIDE, TOP, INGREDIENT
}
