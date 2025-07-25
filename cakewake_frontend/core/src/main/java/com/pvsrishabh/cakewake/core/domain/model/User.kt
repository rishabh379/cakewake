package com.pvsrishabh.cakewake.core.domain.model

data class User(
    val id: String,
    val mobileNumber: String,
    val isVerified: Boolean,
    val profile: String,
    val role: String
)