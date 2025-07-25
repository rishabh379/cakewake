package com.pvsrishabh.cakewake.features.home.data.remote.api

import com.pvsrishabh.cakewake.features.home.data.remote.dto.CakeDto
import com.pvsrishabh.cakewake.features.home.data.remote.dto.CategoryDto
import com.pvsrishabh.cakewake.features.home.data.remote.dto.SpecialOfferDto
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApiService {

    @GET("cakes")
    suspend fun getCakes(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("category") category: String? = null,
        @Query("budget_min") budgetMin: Int? = null,
        @Query("budget_max") budgetMax: Int? = null,
        @Query("search") search: String? = null
    ): List<CakeDto>

    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("special-offers")
    suspend fun getSpecialOffers(): List<SpecialOfferDto>
}