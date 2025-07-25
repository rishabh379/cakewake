package com.pvsrishabh.cakewake.features.auth.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned

@Composable
fun SafeFocusRequester(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    content: @Composable (Modifier) -> Unit
) {
    var isPlaced by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            isPlaced = false
        }
    }

    LaunchedEffect(isPlaced) {
        if (isPlaced) {
            try {
                focusRequester.requestFocus()
            } catch (e: Exception) {
                // Log the error if needed
            }
        }
    }

    content(
        modifier
            .onGloballyPositioned { isPlaced = true }
            .focusRequester(focusRequester)
    )
}
