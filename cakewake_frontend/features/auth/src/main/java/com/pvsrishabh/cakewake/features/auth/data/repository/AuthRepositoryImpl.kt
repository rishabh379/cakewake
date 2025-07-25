package com.pvsrishabh.cakewake.features.auth.data.repository

import com.pvsrishabh.cakewake.core.data.mapper.toDomain
import com.pvsrishabh.cakewake.core.domain.manager.TokenProvider
import com.pvsrishabh.cakewake.core.domain.model.User
import com.pvsrishabh.cakewake.features.auth.data.model.OtpRequest
import com.pvsrishabh.cakewake.features.auth.data.model.PhoneRequest
import com.pvsrishabh.cakewake.features.auth.data.remote.AuthApi
import com.pvsrishabh.cakewake.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenProvider: TokenProvider
) : AuthRepository {

    override suspend fun requestOtp(mobileNumber: String): Boolean {
        val res = authApi.requestOtp(PhoneRequest(mobileNumber))
        return res.isSignup
    }

    override suspend fun verifyOtp(mobileNumber: String, otp: String): Pair<String, User> {
        val res = authApi.verifyOtp(OtpRequest(mobileNumber = mobileNumber, otp = otp))
        // Save token
        tokenProvider.saveToken(res.token)
        return Pair(res.token, res.user.toDomain())
    }
}