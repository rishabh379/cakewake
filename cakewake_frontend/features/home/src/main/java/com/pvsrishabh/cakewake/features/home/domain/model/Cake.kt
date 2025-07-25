package com.pvsrishabh.cakewake.features.home.domain.model

data class Cake(
    val id: String,
    val name: String,
    val price: Double,
    val originalPrice: Double? = null,
    val imageUrl: String,
    val discount: String? = null,
    val categoryId: String? = null,
    val isFeatured: Boolean = false,
    val rating: Float = 0f,
    val description: String = ""
)