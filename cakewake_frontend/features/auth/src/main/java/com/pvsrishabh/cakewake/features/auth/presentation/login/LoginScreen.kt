package com.pvsrishabh.cakewake.features.auth.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvsrishabh.cakewake.features.auth.presentation.components.CountryPickerDialog
import com.pvsrishabh.cakewake.core.utils.countryCodeToEmoji
import com.pvsrishabh.cakewake.core.utils.getCountries
import com.pvsrishabh.cakewake.features.auth.R
import com.pvsrishabh.cakewake.features.auth.presentation.AuthUiState

@Composable
fun LoginScreen(
    state: AuthUiState,
    onPhoneChange: (String) -> Unit,
    onSendOtpClick: () -> Unit,
    onCountryCodeChange: (String) -> Unit
) {
    val purpleColor = Color(0xFF7974db)
    // Country picker state
    var showCountryPicker by remember { mutableStateOf(false) }
    val countries = remember { getCountries() }
    var selectedCountry by remember {
        mutableStateOf(countries.firstOrNull { it.name == "India" } ?: countries.first())
        // Default to India if available, otherwise the first country
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(purpleColor)
    ) {
        // Background cakes image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.cakes_background),
                contentDescription = "Cakes",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            )
            Image(
                painter = painterResource(id = R.drawable.text_cake_wake),
                contentDescription = "Text",
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.Center)
            )
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = (60).dp)
            )
        }

        // Input card - positioned over the cake images
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
                .padding(horizontal = 6.dp),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 22.dp, vertical = 24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Enter your number",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.Black
                )


//              Phone number input field
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = "Mobile Number",
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                        )
                    },
                    value = state.phoneNumber,
                    onValueChange = onPhoneChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    leadingIcon = {
                        // Country code selector with click to open picker
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { showCountryPicker = true }
                        ) {
                            // Country flag
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = countryCodeToEmoji(selectedCountry.code),
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            // Country code
                            Text(
                                text = selectedCountry.dialCode,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorResource(id = R.color.splash_background),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = colorResource(id = R.color.splash_background),
                    ),
                    shape = RoundedCornerShape(12.dp),
                )

                // Get Started button
                Button(
                    onClick = onSendOtpClick,
                    enabled = !state.isLoading && state.phoneNumber.length >= 10,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = purpleColor,
                        disabledContainerColor = purpleColor.copy(alpha = 0.6f)
                    )
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            modifier = Modifier.padding(vertical = 4.dp),
                            text = "Get Started",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Terms and Privacy text
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("By clicking in, I accept the ")
                            pushStyle(
                                SpanStyle(
                                    color = Color.Black,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                            append("terms of services")
                            pop()
                            append(" & ")
                            pushStyle(
                                SpanStyle(
                                    color = Color.Black,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                            append("privacy policy")
                        },
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                state.errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    var text = it
                    if (it.contains('4')) {
                        text = "Enter Phone Number Correctly"
                    } else if (it.contains('5')) {
                        text = "Server Error"
                    }
                    Text(
                        text = text,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Red
                    )
                }
            }
        }
    }

    // Country picker dialog
    if (showCountryPicker) {
        CountryPickerDialog(
            countries = countries,
            onDismiss = { showCountryPicker = false },
            onCountrySelected = { country ->
                selectedCountry = country
                onCountryCodeChange(country.dialCode)
                showCountryPicker = false
            }
        )
    }
}
