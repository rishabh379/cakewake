package com.pvsrishabh.cakewake.features.auth.di

import com.pvsrishabh.cakewake.core.domain.manager.TokenProvider
import com.pvsrishabh.cakewake.features.auth.data.remote.AuthApi
import com.pvsrishabh.cakewake.features.auth.data.repository.AuthRepositoryImpl
import com.pvsrishabh.cakewake.features.auth.domain.repository.AuthRepository
import com.pvsrishabh.cakewake.features.auth.domain.usecases.AuthUseCases
import com.pvsrishabh.cakewake.features.auth.domain.usecases.RequestOtpUseCase
import com.pvsrishabh.cakewake.features.auth.domain.usecases.VerifyOtpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi,
        tokenProvider: TokenProvider
    ): AuthRepository {
        return AuthRepositoryImpl(authApi, tokenProvider)
    }

    @Provides
    @Singleton
    fun provideAuthUseCases(
        authRepository: AuthRepository
    ) = AuthUseCases(
        requestOtp = RequestOtpUseCase(authRepository),
        verifyOtp = VerifyOtpUseCase(authRepository)
    )
}