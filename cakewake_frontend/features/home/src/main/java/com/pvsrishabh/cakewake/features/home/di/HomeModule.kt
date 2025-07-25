package com.pvsrishabh.cakewake.features.home.di

import com.android.identity.securearea.SecureAreaRepository
import com.pvsrishabh.cakewake.core.data.remote.ApiService
import com.pvsrishabh.cakewake.core.data.repository.UserRepositoryImpl
import com.pvsrishabh.cakewake.core.domain.repository.UserRepository
import com.pvsrishabh.cakewake.features.home.data.remote.api.HomeApiService
import com.pvsrishabh.cakewake.features.home.data.repository.HomeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.pvsrishabh.cakewake.features.home.domain.repository.HomeRepository
import com.pvsrishabh.cakewake.features.home.domain.usecases.GetCakesUseCase
import com.pvsrishabh.cakewake.features.home.domain.usecases.GetCategoriesUseCase
import com.pvsrishabh.cakewake.features.home.domain.usecases.GetSpecialOffersUseCase
import com.pvsrishabh.cakewake.features.home.domain.usecases.HomeUseCases
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
object HomeModule {
    @Provides
    @Singleton
    fun provideHomeUseCases(
        homeRepository: HomeRepository
    ) = HomeUseCases(
        getCakes = GetCakesUseCase(homeRepository),
        getCategories = GetCategoriesUseCase(homeRepository),
        getSpecialOffers = GetSpecialOffersUseCase(homeRepository)
    )
    @Provides
    @Singleton
    fun provideHomeRepository(
        apiService: HomeApiService
    ): HomeRepository {
        return HomeRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideHomeApiService(retrofit: Retrofit): HomeApiService = retrofit.create(HomeApiService::class.java)

}