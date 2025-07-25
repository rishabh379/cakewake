package com.pvsrishabh.cakewake.features.cake_customization.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SerializablePointF(
    val x: Float = 0f,
    val y: Float = 0f
)
