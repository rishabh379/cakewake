package com.pvsrishabh.cakewake.features.home.presentation

import androidx.paging.PagingData
import com.pvsrishabh.cakewake.features.home.domain.model.BudgetRange
import com.pvsrishabh.cakewake.features.home.domain.model.Cake
import com.pvsrishabh.cakewake.features.home.domain.model.Category
import com.pvsrishabh.cakewake.features.home.domain.model.SpecialOffer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class HomeState(
    val searchQuery: String = "",
    val categories: List<Category> = emptyList(),
    val selectedCategory: String? = null,
    val specialOffers: List<SpecialOffer> = emptyList(),
    val selectedBudget: BudgetRange? = null,
    val cakes: Flow<PagingData<Cake>> = flowOf(PagingData.empty()),
    val isLoading: Boolean = false,
    val error: String? = null
)