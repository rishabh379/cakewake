package com.pvsrishabh.cakewake.core.domain.model

data class Location(
    val id: String,
    val profileId: String,
    val locationName: String,
    val city: String,
    val state: String,
    val country: String,
    val pincode: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)