package com.pvsrishabh.cakewake.features.feed.presentation

import com.pvsrishabh.cakewake.features.feed.domain.model.CakeCategory
import com.pvsrishabh.cakewake.features.feed.domain.model.FeedPost

data class FeedUiState(
    val isLoading: Boolean = false,
    val cakeCategories: List<CakeCategory> = emptyList(),
    val feedPosts: List<FeedPost> = emptyList(),
    val error: String? = null,
    val isRefreshing: Boolean = false
)