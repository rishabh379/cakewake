package com.pvsrishabh.cakewake.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pvsrishabh.cakewake.features.home.data.local.StaticHomeData
import com.pvsrishabh.cakewake.features.home.domain.model.Cake
import com.pvsrishabh.cakewake.features.home.domain.usecases.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadInitialData()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SearchQueryChanged -> {
                _state.value = _state.value.copy(searchQuery = event.query)
                loadCakes()
            }

            is HomeEvent.CategorySelected -> {
                _state.value = _state.value.copy(selectedCategory = event.categoryId)
                loadCakes()
            }

            is HomeEvent.BudgetSelected -> {
                _state.value = _state.value.copy(selectedBudget = event.budget)
                loadCakes()
            }

            is HomeEvent.ClearError -> {
                _state.value = _state.value.copy(error = null)
            }

            is HomeEvent.Refresh -> {
                loadInitialData()
            }
        }
    }

    private fun loadInitialData() {
        loadCategories()
        loadSpecialOffers()
        loadCakes()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            // Using static data for now - replace with API call later
            try {
                _state.value = _state.value.copy(
                    categories = StaticHomeData.categories,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }

            /* When API is ready, uncomment this:
            homeUseCases.getCategories().collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            categories = resource.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            error = resource.message,
                            isLoading = false
                        )
                    }
                }
            }
            */
        }
    }

    private fun loadSpecialOffers() {
        viewModelScope.launch {
            // Using static data for now - replace with API call later
            try {
                _state.value = _state.value.copy(
                    specialOffers = StaticHomeData.specialOffers,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }

            /* When API is ready, uncomment this:
            homeUseCases.getSpecialOffers().collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            specialOffers = resource.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            error = resource.message,
                            isLoading = false
                        )
                    }
                }
            }
            */
        }
    }

    private fun loadCakes() {
        viewModelScope.launch {
            try {
                // Use static dummy data for now
                val dummyCakes = StaticHomeData.cakes
                val pagingData = PagingData.from(dummyCakes)
                val cakesFlow: Flow<PagingData<Cake>> = flowOf(pagingData)
                _state.value = _state.value.copy(
                    cakes = cakesFlow,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }
}