package com.pvsrishabh.cakewake.features.home.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.pvsrishabh.cakewake.core.utils.Resource
import com.pvsrishabh.cakewake.features.home.data.mapper.toDomain
import com.pvsrishabh.cakewake.features.home.data.remote.api.HomeApiService
import com.pvsrishabh.cakewake.features.home.data.remote.paging.CakesPagingSource
import com.pvsrishabh.cakewake.features.home.domain.model.Cake
import com.pvsrishabh.cakewake.features.home.domain.model.Category
import com.pvsrishabh.cakewake.features.home.domain.model.SpecialOffer
import com.pvsrishabh.cakewake.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val apiService: HomeApiService
) : HomeRepository {

    override fun getCakes(
        category: String?,
        budgetMin: Int?,
        budgetMax: Int?,
        search: String?
    ): Flow<PagingData<Cake>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CakesPagingSource(
                    apiService = apiService,
                    category = category,
                    budgetMin = budgetMin,
                    budgetMax = budgetMax,
                    search = search
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun getCategories(): Flow<Resource<List<Category>>> = flow {
        try {
            emit(Resource.Loading())
            val categories = apiService.getCategories().map { it.toDomain() }
            emit(Resource.Success(categories))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }

    override suspend fun getSpecialOffers(): Flow<Resource<List<SpecialOffer>>> = flow {
        try {
            emit(Resource.Loading())
            val offers = apiService.getSpecialOffers().map { it.toDomain() }
            emit(Resource.Success(offers))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }
}
