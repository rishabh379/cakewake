package com.pvsrishabh.cakewake.features.home.presentation

import com.pvsrishabh.cakewake.features.home.domain.model.BudgetRange

sealed class HomeEvent {
    data class SearchQueryChanged(val query: String) : HomeEvent()
    data class CategorySelected(val categoryId: String?) : HomeEvent()
    data class BudgetSelected(val budget: BudgetRange?) : HomeEvent()
    object ClearError : HomeEvent()
    object Refresh : HomeEvent()
}