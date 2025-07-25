package com.pvsrishabh.cakewake.features.cake_customization.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pvsrishabh.cakewake.features.cake_customization.R
import com.pvsrishabh.cakewake.features.cake_customization.presentation.CakeCustomizationEvent
import com.pvsrishabh.cakewake.features.cake_customization.presentation.CakeCustomizationTab
import com.pvsrishabh.cakewake.features.cake_customization.presentation.CakeCustomizationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    viewModel: CakeCustomizationViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onBuyClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }
        },
        actions = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.onEvent(CakeCustomizationEvent.Undo) },
                    enabled = state.canUndo
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.undo_icon),
                        contentDescription = "Undo",
                        tint = if (state.canUndo) Color.Black else Color.Gray
                    )
                }
                IconButton(
                    onClick = { viewModel.onEvent(CakeCustomizationEvent.Redo) },
                    enabled = state.canRedo
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.redo_icon),
                        contentDescription = "Redo",
                        tint = if (state.canRedo) Color.Black else Color.Gray
                    )
                }
                Button(
                    onClick = onBuyClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6366F1)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Buy", color = Color.White, fontWeight = FontWeight.Medium)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}