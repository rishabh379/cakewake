package com.pvsrishabh.cakewake.features.auth.presentation.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvsrishabh.cakewake.features.auth.R
import com.pvsrishabh.cakewake.features.auth.presentation.components.SafeFocusRequester
import com.pvsrishabh.cakewake.features.auth.presentation.AuthUiState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPVerificationScreen(
    state: AuthUiState,
    onOtpChange: (String) -> Unit,
    onBackClick: () -> Unit = {},
    onVerifyClick: () -> Unit,
    onResendClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(state.otp.length){
        if(state.otp.length == 6) {
            delay(200) // Small delay to prevent accidental submissions
            onVerifyClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("OTP Verification") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "We have sent a verification code to",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )

            Text(
                text = state.countryCode + " " + state.phoneNumber,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Hidden input
            SafeFocusRequester(
                modifier = Modifier.fillMaxWidth(),
                focusRequester = focusRequester
            ) { focusModifier ->
                BasicTextField(
                    value = state.otp,
                    onValueChange = { onOtpChange(it) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        autoCorrect = false
                    ),
                    modifier = focusModifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                )
            }

            // OTP boxes row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(6) { index ->
                    val digit = state.otp.getOrNull(index)?.toString() ?: ""
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(48.dp)
                            .border(
                                width = if (digit.isNotEmpty()) 2.dp else 1.dp,
                                color = if (digit.isNotEmpty()) colorResource(R.color.splash_background) else Color.LightGray,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Text(
                            text = digit,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.height(18.dp))
            }

            if (state.secondsLeft > 0) {
                Text("Resend available in ${state.secondsLeft}s",
                    color = Color.Gray,
                    fontWeight = FontWeight.Black)
            } else {
                TextButton(onClick = onResendClick) {
                    Text("Resend OTP",
                    fontSize = 16.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Black)
                }
            }

            state.errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                var text = it
                if(it.contains('4')){
                    text = "Incorrect OTP"
                }else if(it.contains('5')) {
                    text = "Server Error"
                }
                Text(text = text, color = Color.Red)
            }
        }
    }
}