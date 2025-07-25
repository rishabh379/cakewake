package com.pvsrishabh.cakewake.app.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pvsrishabh.cakewake.core.navigation.Route
import com.pvsrishabh.cakewake.core.navigation.bottomNavItems
import com.pvsrishabh.cakewake.features.feed.presentation.FeedScreen
import com.pvsrishabh.cakewake.features.home.presentation.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CakeWakeNavigator(
    navController: NavController,
    currentScreen: String
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = Modifier.fillMaxSize().navigationBarsPadding(),
        bottomBar = {
            CustomBottomNavigation(
                currentDestination = currentDestination,
                onNavigate = { route ->
                    if (currentDestination?.route != route) {
                        navController.navigate(route) {
                            popUpTo(Route.HomeScreen.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        when (currentScreen) {
            "home" -> HomeScreen(
                onNavigateToDesign = {
                    navController.navigate(Route.CreateCakeScreen.route)
                },
                onNavigateToProfile = {
//                    navController.navigate(Route.ProfileScreen.route)
                },
                onNavigateToCakeDetail = { cakeId ->
//                    navController.navigate(Route.CakeDetailScreen.createRoute(cakeId))
                },
                modifier = Modifier.padding(innerPadding)
            )

            "feed" -> FeedScreen(
                onNavigateToProfile = {
//                    navController.navigate(Route.ProfileScreen.route)
                },
                onNavigateToCakeDetail = { cakeId ->
//                    navController.navigate(Route.CakeDetailScreen.createRoute(cakeId))
                },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun CakeWakeBottomNavigation(
    navController: NavController,
    currentDestination: String?
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            val isSelected = when {
                item.isFullScreen -> false // Design tab never shows as selected in bottom nav
                else -> currentDestination == item.route
            }

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = isSelected,
                onClick = {
                    when {
                        item.isFullScreen -> {
                            // Navigate to full screen design
                            navController.navigate(item.route)
                        }

                        currentDestination != item.route -> {
                            // Navigate between Home and Feed
                            navController.navigate(item.route) {
                                popUpTo(Route.HomeScreen.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                }
            )
        }
    }
}