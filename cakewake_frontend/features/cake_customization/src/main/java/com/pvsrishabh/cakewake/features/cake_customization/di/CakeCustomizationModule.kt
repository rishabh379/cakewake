package com.pvsrishabh.cakewake.features.cake_customization.di

import android.app.Application
import androidx.room.Room
import com.pvsrishabh.cakewake.features.cake_customization.data.local.database.CakeDao
import com.pvsrishabh.cakewake.features.cake_customization.data.local.database.CakeDatabase
import com.pvsrishabh.cakewake.features.cake_customization.data.repository.CakeRepositoryImpl
import com.pvsrishabh.cakewake.features.cake_customization.domain.repository.CakeRepository
import com.pvsrishabh.cakewake.features.cake_customization.domain.usecases.CakeCustomizationUseCases
import com.pvsrishabh.cakewake.features.cake_customization.domain.usecases.DeleteCakeCustomization
import com.pvsrishabh.cakewake.features.cake_customization.domain.usecases.GetAllCakeCustomizations
import com.pvsrishabh.cakewake.features.cake_customization.domain.usecases.LoadCakeCustomization
import com.pvsrishabh.cakewake.features.cake_customization.domain.usecases.SaveCakeCustomization
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CakeCustomizationModule {

    @Provides
    @Singleton
    fun provideCakeDao(database: CakeDatabase): CakeDao {
        return database.cakeDao
    }

    @Provides
    @Singleton
    fun provideCakeDatabase(application: Application): CakeDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = CakeDatabase::class.java,
            name = "cake_customization_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCakeRepository(
        cakeDao: CakeDao
    ): CakeRepository {
        return CakeRepositoryImpl(cakeDao)
    }

    @Provides
    @Singleton
    fun provideCakeCustomizationUseCases(
        cakeRepository: CakeRepository
    ): CakeCustomizationUseCases {
        return CakeCustomizationUseCases(
            saveCakeCustomization = SaveCakeCustomization(cakeRepository),
            getAllCakeCustomizations = GetAllCakeCustomizations(cakeRepository),
            loadCakeCustomization = LoadCakeCustomization(cakeRepository),
            deleteCakeCustomization = DeleteCakeCustomization(
                cakeRepository
            )
        )
    }
}