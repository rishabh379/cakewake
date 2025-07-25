package com.pvsrishabh.cakewake.features.auth.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pvsrishabh.cakewake.core.domain.model.User
import com.pvsrishabh.cakewake.core.domain.usecases.profile.ProfileUseCases
import com.pvsrishabh.cakewake.core.domain.usecases.user_entry.UserEntryUseCases
import com.pvsrishabh.cakewake.features.auth.domain.usecases.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val profileUseCases: ProfileUseCases,
    private val userEntryUseCases: UserEntryUseCases
) : ViewModel() {

    var state by mutableStateOf(AuthUiState())
        private set

    private var timerJob: Job? = null

    // Temporarily hold user for sign-up until profile is completed
    private var tempUser: User? = null

    fun onPhoneNumberChange(number: String) {
        state = state.copy(phoneNumber = number)
    }

    fun onOtpChange(otp: String) {
        if (otp.length <= 6 && otp.all { it.isDigit() }) {
            state = state.copy(otp = otp)
        }
    }

    fun onCountryCodeChange(countryCode: String) {
        state = state.copy(countryCode = countryCode)
    }

    fun onNameChange(name: String) {
        state = state.copy(name = name)
    }

    fun onEmailChange(email: String) {
        state = state.copy(email = email)
    }

    fun sendOtp(onSuccess: () -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null)
            try {
                val isSignUp = authUseCases.requestOtp(state.phoneNumber)
                state = state.copy(
                    isOtpSent = true,
                    isLoading = false,
                    secondsLeft = 25,
                    isSignUp = isSignUp
                )
                startResendTimer()
                onSuccess()
            } catch (e: Exception) {
                state = state.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun getTempUser(): User? {
        return tempUser
    }

    fun verifyOtp(onVerified: (Boolean) -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null)
            try {
                val (token, user) = authUseCases.verifyOtp(state.phoneNumber, state.otp)
                if(!state.isSignUp) {
                    userEntryUseCases.saveUserEntry(
                        id = user.id,
                        mobileNumber = user.mobileNumber,
                        isVerified = user.isVerified,
                        profile = user.profile,
                        role = user.role
                    )
                }else {
                    // Sign-up user: store for later use
                    tempUser = user
                }
                state = state.copy(
                    isOtpVerified = true,
                    isLoading = false
                )
                // Pass the current value explicitly
                onVerified(state.isSignUp)
            } catch (e: Exception) {
                state = state.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    suspend fun saveUserEntry(user: User) {
        try {
            userEntryUseCases.saveUserEntry(
                id = user.id,
                mobileNumber = user.mobileNumber,
                isVerified = user.isVerified,
                profile = user.profile,
                role = user.role
            )
        } catch (e: Exception) {
            // Log or handle the exception if needed
            state = state.copy(errorMessage = e.message)
        }
    }

    fun updateProfile(onComplete: () -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, errorMessage = null)
            try {
                tempUser?.let { user ->
                    profileUseCases.updateProfile(
                        name = state.name,
                        email = state.email,
                        profileId = user.profile,
                        image = ""
                    )
                    // After profile update, now save the user credentials (for sign-up users)
                    saveUserEntry(user)
                }
                state = state.copy(isLoading = false)
                onComplete()
            } catch (e: Exception) {
                state = state.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun resetTimer() {
        sendOtp {  }
    }

    private fun startResendTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (state.secondsLeft > 0) {
                delay(1000)
                state = state.copy(secondsLeft = state.secondsLeft - 1)
            }
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}

