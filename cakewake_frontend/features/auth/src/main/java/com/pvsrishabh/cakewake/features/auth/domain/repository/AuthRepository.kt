package com.pvsrishabh.cakewake.features.auth.domain.repository

import com.pvsrishabh.cakewake.core.domain.model.User

interface AuthRepository {
    suspend fun requestOtp(mobileNumber: String): Boolean
    suspend fun verifyOtp(mobileNumber: String, otp: String): Pair<String, User> // (token, user)
}