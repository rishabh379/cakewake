package com.pvsrishabh.cakewake.features.home.domain.model

data class Category(
    val id: String,
    val name: String,
    val imageUrl: String,
    val color: String? = null
)