package com.pvsrishabh.cakewake.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String,
    val isFullScreen: Boolean = false
)

val bottomNavItems = listOf(
    BottomNavItem(
        title = "Home",
        icon = Icons.Default.Home,
        route = Route.HomeScreen.route
    ),
    BottomNavItem(
        title = "Create",
        icon = Icons.Default.Add,
        route = Route.CreateCakeScreen.route,
        isFullScreen = true
    ),
    BottomNavItem(
        title = "Feed",
        icon = Icons.Default.Favorite,
        route = Route.FeedScreen.route
    )
)
