package com.pvsrishabh.cakewake.features.auth.domain.usecases

import com.pvsrishabh.cakewake.core.domain.model.User
import com.pvsrishabh.cakewake.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(mobileNumber: String, otp: String): Pair<String, User> {
        return authRepository.verifyOtp(mobileNumber, otp)
    }
}