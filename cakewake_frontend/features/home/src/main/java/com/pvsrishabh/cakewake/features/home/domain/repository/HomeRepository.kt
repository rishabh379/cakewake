package com.pvsrishabh.cakewake.features.home.domain.repository

import androidx.paging.PagingData
import com.pvsrishabh.cakewake.core.utils.Resource
import com.pvsrishabh.cakewake.features.home.domain.model.Cake
import com.pvsrishabh.cakewake.features.home.domain.model.Category
import com.pvsrishabh.cakewake.features.home.domain.model.SpecialOffer
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getCakes(
        category: String? = null,
        budgetMin: Int? = null,
        budgetMax: Int? = null,
        search: String? = null
    ): Flow<PagingData<Cake>>

    suspend fun getCategories(): Flow<Resource<List<Category>>>

    suspend fun getSpecialOffers(): Flow<Resource<List<SpecialOffer>>>
}