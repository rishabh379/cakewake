package com.pvsrishabh.cakewake.core.di

import android.app.Application
import android.content.Context
import com.pvsrishabh.cakewake.core.data.local.manager.LocalAppEntryManagerImpl
import com.pvsrishabh.cakewake.core.data.local.manager.LocalUserEntryManagerImpl
import com.pvsrishabh.cakewake.core.data.local.preferences.UserPreferences
import com.pvsrishabh.cakewake.core.data.remote.ApiService
import com.pvsrishabh.cakewake.core.data.local.manager.TokenProviderImpl
import com.pvsrishabh.cakewake.core.data.repository.UserRepositoryImpl
import com.pvsrishabh.cakewake.core.domain.manager.LocalAppEntryManager
import com.pvsrishabh.cakewake.core.domain.manager.LocalUserEntryManager
import com.pvsrishabh.cakewake.core.domain.manager.TokenProvider
import com.pvsrishabh.cakewake.core.domain.repository.UserRepository
import com.pvsrishabh.cakewake.core.domain.usecases.app_entry.AppEntryUseCases
import com.pvsrishabh.cakewake.core.domain.usecases.app_entry.ReadAppEntry
import com.pvsrishabh.cakewake.core.domain.usecases.app_entry.SaveAppEntry
import com.pvsrishabh.cakewake.core.domain.usecases.location.AddLocationUseCase
import com.pvsrishabh.cakewake.core.domain.usecases.location.DeleteLocationUseCase
import com.pvsrishabh.cakewake.core.domain.usecases.location.GetLocationsUseCase
import com.pvsrishabh.cakewake.core.domain.usecases.location.LocationUseCases
import com.pvsrishabh.cakewake.core.domain.usecases.location.UpdateLocationUseCase
import com.pvsrishabh.cakewake.core.domain.usecases.profile.DeleteProfileUseCase
import com.pvsrishabh.cakewake.core.domain.usecases.profile.GetProfileUseCase
import com.pvsrishabh.cakewake.core.domain.usecases.profile.ProfileUseCases
import com.pvsrishabh.cakewake.core.domain.usecases.profile.UpdateProfileUseCase
import com.pvsrishabh.cakewake.core.domain.usecases.user.DeleteUserUseCase
import com.pvsrishabh.cakewake.core.domain.usecases.user.GetUserUseCase
import com.pvsrishabh.cakewake.core.domain.usecases.user.UserUseCases
import com.pvsrishabh.cakewake.core.domain.usecases.user_entry.ClearUserEntry
import com.pvsrishabh.cakewake.core.domain.usecases.user_entry.ReadUserEntry
import com.pvsrishabh.cakewake.core.domain.usecases.user_entry.SaveUserEntry
import com.pvsrishabh.cakewake.core.domain.usecases.user_entry.UserEntryUseCases
import com.pvsrishabh.cakewake.core.utils.Constants.BASE_URL
import com.pvsrishabh.cakewake.core.utils.TokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    @Provides
    @Singleton
    fun provideLocalAppEntryManager(
        application: Application
    ): LocalAppEntryManager = LocalAppEntryManagerImpl(application)

    @Provides
    @Singleton
    fun provideLocalUserEntryManager(
        application: Application
    ): LocalUserEntryManager = LocalUserEntryManagerImpl(application)

    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localAppEntryManager: LocalAppEntryManager
    ) = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localAppEntryManager),
        saveAppEntry = SaveAppEntry(localAppEntryManager)
    )

    @Provides
    @Singleton
    fun provideUserEntryUseCases(
        localUserEntryManager: LocalUserEntryManager
    ) = UserEntryUseCases(
        readUserEntry = ReadUserEntry(
            localUserEntryManager
        ),
        saveUserEntry = SaveUserEntry(localUserEntryManager),
        clearUserEntry = ClearUserEntry(localUserEntryManager)
    )

    @Provides
    @Singleton
    fun provideUserUseCases(
        userRepository: UserRepository
    ) = UserUseCases(
        getUser = GetUserUseCase(userRepository),
        deleteUser = DeleteUserUseCase(userRepository)
    )

    @Provides
    @Singleton
    fun provideProfileUseCases(userRepository: UserRepository): ProfileUseCases {
        return ProfileUseCases(
            getProfile = GetProfileUseCase(userRepository),
            updateProfile = UpdateProfileUseCase(userRepository),
            deleteProfile = DeleteProfileUseCase(userRepository)
        )
    }

    @Provides
    @Singleton
    fun provideLocationUseCases(userRepository: UserRepository): LocationUseCases {
        return LocationUseCases(
            addLocation = AddLocationUseCase(userRepository),
            getLocations = GetLocationsUseCase(userRepository),
            updateLocation = UpdateLocationUseCase(userRepository),
            deleteLocation = DeleteLocationUseCase(userRepository)
        )
    }

    // Network Module
    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }

    @Provides
    @Singleton
    fun provideTokenProvider(userPreferences: UserPreferences): TokenProvider {
        return TokenProviderImpl(userPreferences)
    }

    @Provides
    @Singleton
    fun provideTokenInterceptor(tokenProvider: TokenProvider): TokenInterceptor {
        return TokenInterceptor(tokenProvider)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        tokenInterceptor: TokenInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(tokenInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideUserRepository(
        apiService: ApiService
    ): UserRepository {
        return UserRepositoryImpl(apiService)
    }
}