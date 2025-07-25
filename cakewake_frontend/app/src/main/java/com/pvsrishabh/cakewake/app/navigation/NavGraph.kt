package com.pvsrishabh.cakewake.app.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.pvsrishabh.cakewake.core.navigation.Route
import com.pvsrishabh.cakewake.features.auth.navigation.authNavigation
import com.pvsrishabh.cakewake.features.auth.presentation.AuthViewModel
import com.pvsrishabh.cakewake.features.cake_customization.presentation.CakeCustomizationScreen
import com.pvsrishabh.cakewake.features.onboarding.presentation.OnBoardingScreen
import com.pvsrishabh.cakewake.features.onboarding.presentation.OnBoardingViewModel

@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val onBoardingViewModel: OnBoardingViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = startDestination) {

        // OnBoarding navigation
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route
        ) {
            composable(
                route = Route.OnBoardingScreen.route
            ) {
                OnBoardingScreen(
                    event = onBoardingViewModel::onEvent
                )
            }
        }

        // Auth navigation
        authNavigation(navController, authViewModel)

        // Main app navigation
        navigation(
            route = Route.CakeWakeNavigation.route,
            startDestination = Route.HomeScreen.route
        ) {
            composable(route = Route.HomeScreen.route) {
                CakeWakeNavigator(
                    navController = navController,
                    currentScreen = "home"
                )
            }
            composable(route = Route.FeedScreen.route) {
                CakeWakeNavigator(
                    navController = navController,
                    currentScreen = "feed"
                )
            }
            composable(route = Route.CreateCakeScreen.route) {
                CakeCustomizationScreen(
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }

//            composable(route = Route.CakeDetailScreen.route) { backStackEntry ->
//                val cakeId = backStackEntry.arguments?.getString("cakeId") ?: ""
//                CakeDetailScreen(
//                    cakeId = cakeId,
//                    onNavigateBack = {
//                        navController.navigateUp()
//                    },
//                    onNavigateToBuy = {
//                        // Handle buy navigation
//                    }
//                )
//            }
//
//            composable(route = Route.ProfileScreen.route) {
//                ProfileScreen(
//                    onNavigateBack = {
//                        navController.navigateUp()
//                    }
//                )
//            }
        }
    }
}