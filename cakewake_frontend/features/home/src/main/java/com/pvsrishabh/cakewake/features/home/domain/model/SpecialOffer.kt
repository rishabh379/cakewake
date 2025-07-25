package com.pvsrishabh.cakewake.features.home.domain.model

data class SpecialOffer(
    val id: String,
    val title: String,
    val description: String? = null,
    val cakes: List<Cake>,
    val backgroundColor: String? = null
)