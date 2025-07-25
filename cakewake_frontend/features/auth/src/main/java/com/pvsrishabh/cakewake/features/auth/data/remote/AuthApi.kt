package com.pvsrishabh.cakewake.features.auth.data.remote

import com.pvsrishabh.cakewake.core.data.model.OtpResponse
import com.pvsrishabh.cakewake.core.data.model.SignUpOrLoginResponse
import com.pvsrishabh.cakewake.features.auth.data.model.OtpRequest
import com.pvsrishabh.cakewake.features.auth.data.model.PhoneRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    // Auth
    @POST("signup-or-login")
    suspend fun requestOtp(@Body body: PhoneRequest): SignUpOrLoginResponse

    @POST("verify-otp")
    suspend fun verifyOtp(@Body body: OtpRequest): OtpResponse
}