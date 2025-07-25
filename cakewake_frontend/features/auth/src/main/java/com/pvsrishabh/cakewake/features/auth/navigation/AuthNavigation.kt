package com.pvsrishabh.cakewake.features.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.lifecycle.viewModelScope
import com.pvsrishabh.cakewake.core.navigation.Route
import com.pvsrishabh.cakewake.features.auth.presentation.AuthViewModel
import com.pvsrishabh.cakewake.features.auth.presentation.login.LoginScreen
import com.pvsrishabh.cakewake.features.auth.presentation.profile.ProfileInputScreen
import com.pvsrishabh.cakewake.features.auth.presentation.verification.OTPVerificationScreen
import kotlinx.coroutines.launch

fun NavGraphBuilder.authNavigation(navController: NavController, viewModel: AuthViewModel) {
    navigation(
        route = Route.Auth.route,
        startDestination = Route.LoginScreen.route
    ) {
        composable(Route.LoginScreen.route) {
            LoginScreen(
                state = viewModel.state,
                onPhoneChange = viewModel::onPhoneNumberChange,
                onSendOtpClick = {
                    viewModel.sendOtp {
                        navController.navigate(Route.OtpVerificationScreen.route)
                    }
                },
                onCountryCodeChange = viewModel::onCountryCodeChange
            )
        }

        composable(Route.OtpVerificationScreen.route) {
            OTPVerificationScreen(
                state = viewModel.state,
                onOtpChange = viewModel::onOtpChange,
                onVerifyClick = {
                    viewModel.verifyOtp { isSignUp ->
                        if (isSignUp) {
                            navController.navigate(Route.ProfileInputScreen.route)
                        } else {
                            navController.navigate(Route.CakeWakeNavigation.route) {
                                popUpTo(Route.Auth.route) { inclusive = true }
                            }
                        }
                    }
                },
                onResendClick = viewModel::resetTimer,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Route.ProfileInputScreen.route) {
            ProfileInputScreen(
                state = viewModel.state,
                onNameChange = viewModel::onNameChange,
                onEmailChange = viewModel::onEmailChange,
                onContinueClick = {
                    viewModel.updateProfile {
                        navController.navigate(Route.CakeWakeNavigation.route) {
                            popUpTo(Route.Auth.route) { inclusive = true }
                        }
                    }
                },
                onSkipClick = {
                    viewModel.getTempUser()?.let { user ->
                        viewModel.viewModelScope.launch {
                            viewModel.saveUserEntry(user)
                        }
                    }
                    navController.navigate(Route.CakeWakeNavigation.route) {
                        popUpTo(Route.Auth.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
