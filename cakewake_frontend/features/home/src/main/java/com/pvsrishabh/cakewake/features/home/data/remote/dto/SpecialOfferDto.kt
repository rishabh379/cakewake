package com.pvsrishabh.cakewake.features.home.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SpecialOfferDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("cakes")
    val cakes: List<CakeDto>,
    @SerializedName("background_color")
    val backgroundColor: String?
)