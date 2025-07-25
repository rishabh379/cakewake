package com.pvsrishabh.cakewake.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.pvsrishabh.cakewake.features.home.presentation.components.BannerSection
import com.pvsrishabh.cakewake.features.home.presentation.components.BudgetSection
import com.pvsrishabh.cakewake.features.home.presentation.components.CakeItem
import com.pvsrishabh.cakewake.features.home.presentation.components.CakeItemShimmer
import com.pvsrishabh.cakewake.features.home.presentation.components.ErrorSection
import com.pvsrishabh.cakewake.features.home.presentation.components.FestivalOffersSection
import com.pvsrishabh.cakewake.features.home.presentation.components.FestivalOffersShimmer
import com.pvsrishabh.cakewake.features.home.presentation.components.HomeHeader
import com.pvsrishabh.cakewake.features.home.presentation.components.LuxurySection
import com.pvsrishabh.cakewake.features.home.presentation.components.LuxurySectionShimmer
import com.pvsrishabh.cakewake.features.home.presentation.components.MoodCategoriesSection
import com.pvsrishabh.cakewake.features.home.presentation.components.MoodCategoriesShimmer
import com.pvsrishabh.cakewake.features.home.presentation.components.SearchBar
import com.pvsrishabh.cakewake.features.home.presentation.components.SectionHeader
import com.pvsrishabh.cakewake.features.home.presentation.components.SpecialsSection
import com.pvsrishabh.cakewake.features.home.presentation.components.SpecialsSectionShimmer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

// Updated HomeScreen.kt
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetail: (String) -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToDesign: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCakeDetail: (String) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val cakes = state.cakes.collectAsLazyPagingItems()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        // Header with rounded bottom corners
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White,
                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = Color(0xFFFF6B9D),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Lulu Mall",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Location dropdown",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Gray
                        )
                    }

                    IconButton(
                        onClick = onNavigateToProfile,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            modifier = Modifier.size(32.dp),
                            tint = Color.Gray
                        )
                    }
                }

                // Subtitle
                Text(
                    text = "Fastest Cake Delivery, India",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 24.dp, top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Search Bar
                SearchBar(
                    query = state.searchQuery,
                    onQueryChange = { viewModel.onEvent(HomeEvent.SearchQueryChanged(it)) },
                    onSearchClick = onNavigateToSearch,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Banner Section
                BannerSection(onNavigateToDesign = onNavigateToDesign)
            }
        }

        // Main Content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Mood Categories
            item {
                if (state.categories.isNotEmpty()) {
                    MoodCategoriesSection(
                        categories = state.categories,
                        selectedCategory = state.selectedCategory,
                        onCategoryClick = {
                            viewModel.onEvent(HomeEvent.CategorySelected(it))
                        }
                    )
                } else {
                    MoodCategoriesShimmer()
                }
            }

            // CakeWake Specials
            item {
                if (state.specialOffers.isNotEmpty()) {
                    val specialsSection = state.specialOffers.find { it.title.contains("Specials") }
                    specialsSection?.let { special ->
                        SpecialsSection(
                            title = "CakeWake Specials",
                            titleColor = Color(0xFFFF6B9D),
                            cakes = special.cakes,
                            onCakeClick = onNavigateToDetail
                        )
                    }
                } else {
                    SpecialsSectionShimmer(title = "CakeWake Specials")
                }
            }

            // Festival Offers
            item {
                FestivalOffersSection(
                    offers = state.specialOffers.filter {
                        it.title.contains("Holi") ||
                                it.title.contains("Dewali") ||
                                it.title.contains("Eid")
                    },
                    onOfferClick = { /* Handle offer click */ }
                )
            }

            // Love Bites Section
            item {
                if (state.specialOffers.isNotEmpty()) {
                    val loveBitesSection = state.specialOffers.find { it.title.contains("Love Bites") }
                    loveBitesSection?.let { special ->
                        SpecialsSection(
                            title = "CakeWake Love Bites",
                            titleColor = Color(0xFFE91E63),
                            cakes = special.cakes,
                            onCakeClick = onNavigateToDetail
                        )
                    }
                } else {
                    SpecialsSectionShimmer(title = "CakeWake Love Bites")
                }
            }

            // Budget Section
            item {
                BudgetSection(
                    selectedBudget = state.selectedBudget,
                    onBudgetClick = {
                        viewModel.onEvent(HomeEvent.BudgetSelected(it))
                    }
                )
            }

            // Everyday Cakes
            item {
                SectionHeader(
                    title = "CakeWake EveryDay",
                    titleColor = Color(0xFF2196F3)
                )
            }

            // Paged Cakes
            when (cakes.loadState.refresh) {
                is LoadState.Loading -> {
                    items(6) {
                        CakeItemShimmer()
                    }
                }
                is LoadState.Error -> {
                    item {
                        ErrorSection(
                            message = "Failed to load cakes",
                            onRetry = { cakes.retry() }
                        )
                    }
                }
                else -> {
                    items(count = cakes.itemCount) { index ->
                        val cake = cakes[index]
                        cake?.let {
                            CakeItem(
                                cake = it,
                                onClick = { onNavigateToDetail(it.id) }
                            )
                        }
                    }
                }
            }

            // Loading more indicator
            if (cakes.loadState.append is LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFFFF6B9D))
                    }
                }
            }

            // Luxury Section
            item {
                if (state.specialOffers.isNotEmpty()) {
                    val luxurySection = state.specialOffers.find { it.title.contains("Luxury") }
                    luxurySection?.let { special ->
                        LuxurySection(
                            cakes = special.cakes,
                            onCakeClick = onNavigateToDetail
                        )
                    }
                } else {
                    LuxurySectionShimmer()
                }
            }

            // Sugarless Section
            item {
                if (state.specialOffers.isNotEmpty()) {
                    val sugarlessSection = state.specialOffers.find { it.title.contains("Sugarless") }
                    sugarlessSection?.let { special ->
                        SpecialsSection(
                            title = "CakeWake Sugarless",
                            titleColor = Color(0xFF4CAF50),
                            cakes = special.cakes,
                            onCakeClick = onNavigateToDetail
                        )
                    }
                } else {
                    SpecialsSectionShimmer(title = "CakeWake Sugarless")
                }
            }
        }
    }

    // Handle errors
    state.error?.let { error ->
        LaunchedEffect(error) {
            // Show snackbar or handle error
            viewModel.onEvent(HomeEvent.ClearError)
        }
    }
}