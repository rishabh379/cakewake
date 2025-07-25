package com.pvsrishabh.cakewake.app.main

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pvsrishabh.cakewake.core.domain.usecases.app_entry.AppEntryUseCases
import com.pvsrishabh.cakewake.core.navigation.Route
import com.pvsrishabh.cakewake.core.domain.usecases.user_entry.UserEntryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.pvsrishabh.cakewake.core.utils.isNetworkAvailable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val appEntryUseCases: AppEntryUseCases,
    private val userEntryUseCases: UserEntryUseCases
) : AndroidViewModel(application) {

    var splashCondition by mutableStateOf(true)
        private set

    var startDestination by mutableStateOf(Route.AppStartNavigation.route)
        private set

    var toastMessage by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            if (isNetworkAvailable(getApplication())) {
                try {
                    appEntryUseCases.readAppEntry().onEach { hasCompletedOnboarding ->

                        val credential = userEntryUseCases.readUserEntry().firstOrNull()

                        startDestination = when {
                            !hasCompletedOnboarding -> Route.AppStartNavigation.route
                            credential?.id?.isNotBlank() != true -> Route.Auth.route
                            else -> Route.CakeWakeNavigation.route
                        }

                        delay(1000)
                        splashCondition = false
                    }.launchIn(viewModelScope)
                } catch (e: Exception) {
                    toastMessage = "Error: ${e.message}"
                    startDestination = Route.Auth.route
                    delay(1000)
                    splashCondition = false
                }
            } else {
                toastMessage = "No internet connection. Please check your network settings."
                startDestination = Route.Auth.route
                delay(1000)
                splashCondition = false
            }
        }
    }

    fun resetToastMessage() {
        toastMessage = null
    }
}