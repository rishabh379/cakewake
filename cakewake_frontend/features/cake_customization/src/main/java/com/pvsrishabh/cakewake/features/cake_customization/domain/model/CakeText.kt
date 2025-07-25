package com.pvsrishabh.cakewake.features.cake_customization.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CakeText(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val position: SerializablePointF = SerializablePointF(0f, 0f),
    val size: Float = 16f, // sp
    val color: Int = 0xFF000000.toInt(), // ARGB
    val rotation: Float = 0f,
    val zIndex: Int = 0
)
