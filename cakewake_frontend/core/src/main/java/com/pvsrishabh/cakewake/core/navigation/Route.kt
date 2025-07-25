package com.pvsrishabh.cakewake.core.navigation

sealed class Route(
    val route: String
){

    // Onboarding Screens
    object AppStartNavigation : Route(route = "appStartNavigation")
    object OnBoardingScreen : Route(route = "onBoardingScreen")

    // Authentication Screens
    object Auth : Route("auth")
    object LoginScreen : Route(route = "loginScreen")
    object OtpVerificationScreen : Route(route = "otpVerificationScreen")
    object ProfileInputScreen : Route(route = "profileInputScreen")

    // Main Screens
    object CakeWakeNavigation : Route(route = "cakeWakeNavigation")
    object HomeScreen : Route(route = "homeScreen")
    object CreateCakeScreen : Route(route = "createCakeScreen")
    object FeedScreen : Route(route = "feedScreen")
    object SearchScreen : Route(route = "searchScreen")

}