package com.pvsrishabh.cakewake.features.auth.presentation

data class AuthUiState(
    val phoneNumber: String = "9131548537",
    val countryCode: String = "+91",
    val otp: String = "",
    val name: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val secondsLeft: Int = 25,
    val isOtpSent: Boolean = false,
    val isOtpVerified: Boolean = false,
    val isSignUp: Boolean = false,
    val errorMessage: String? = null
)
