package com.pvsrishabh.cakewake.core.domain.model

data class Profile(
    val id: String,
    val email: String,
    val name: String,
    val image: String,
    val locations: List<Location>
)