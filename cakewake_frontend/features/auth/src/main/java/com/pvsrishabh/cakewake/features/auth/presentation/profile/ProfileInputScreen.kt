package com.pvsrishabh.cakewake.features.auth.presentation.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvsrishabh.cakewake.features.auth.R
import com.pvsrishabh.cakewake.features.auth.presentation.AuthUiState

@Composable
fun ProfileInputScreen(
    state: AuthUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onContinueClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(top = 20.dp)
    ) {
        // Skip button in top-right corner
        TextButton(
            onClick = onSkipClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(
                text = "Skip",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = colorResource(id = R.color.display_small),
            )
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 70.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Header text
            Text(
                text = "We'd love to know you better",
                fontSize = 24.sp,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold,
                color = colorResource(id = R.color.display_small),
                modifier = Modifier.padding(bottom = 22.dp)
            )

            OutlinedTextField(
                value = state.name,
                onValueChange = { onNameChange(it) },
                label = {
                    Text(
                        text = "Name",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280),
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7F7CF9),
                    unfocusedBorderColor = Color(0xFFE5E7EB)
                ),
                textStyle = TextStyle(fontSize = 16.sp),
                singleLine = true
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = { onEmailChange(it) },
                label = {
                    Text(
                        text = "Email (Optional)",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280),
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7F7CF9),
                    unfocusedBorderColor = Color(0xFFE5E7EB)
                ),
                textStyle = TextStyle(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                placeholder = { Text("your@email.com") },
                singleLine = true
            )

            // Continue button
            Button(
                onClick = onContinueClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7F7CF9)
                )
            ) {
                Text(
                    text = "Continue",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}