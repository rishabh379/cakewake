package com.pvsrishabh.cakewake.features.auth.domain.usecases

import com.pvsrishabh.cakewake.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class RequestOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(mobileNumber: String): Boolean {
        return authRepository.requestOtp(mobileNumber)
    }
}