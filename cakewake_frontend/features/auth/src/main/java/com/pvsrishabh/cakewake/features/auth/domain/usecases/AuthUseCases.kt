package com.pvsrishabh.cakewake.features.auth.domain.usecases

data class AuthUseCases(
    val requestOtp: RequestOtpUseCase,
    val verifyOtp: VerifyOtpUseCase
)