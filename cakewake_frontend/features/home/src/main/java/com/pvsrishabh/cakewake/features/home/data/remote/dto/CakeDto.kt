package com.pvsrishabh.cakewake.features.home.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CakeDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("original_price")
    val originalPrice: Double?,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("discount")
    val discount: String?,
    @SerializedName("category_id")
    val categoryId: String?,
    @SerializedName("is_featured")
    val isFeatured: Boolean = false,
    @SerializedName("rating")
    val rating: Float = 0f,
    @SerializedName("description")
    val description: String = ""
)