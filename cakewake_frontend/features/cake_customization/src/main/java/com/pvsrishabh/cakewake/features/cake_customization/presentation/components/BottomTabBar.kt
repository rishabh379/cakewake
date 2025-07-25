package com.pvsrishabh.cakewake.features.cake_customization.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvsrishabh.cakewake.features.cake_customization.R
import com.pvsrishabh.cakewake.features.cake_customization.presentation.CakeCustomizationTab

@Composable
fun BottomTabBar(
    selectedTab: CakeCustomizationTab,
    onTabSelected: (CakeCustomizationTab) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TabItem(
                title = "Base",
                painter = painterResource(R.drawable.base_icon),
                isSelected = selectedTab == CakeCustomizationTab.BASE,
                onClick = { onTabSelected(CakeCustomizationTab.BASE) }
            )
            TabItem(
                title = "Layers",
                painter = painterResource(R.drawable.layers_icon),
                isSelected = selectedTab == CakeCustomizationTab.LAYERS,
                onClick = { onTabSelected(CakeCustomizationTab.LAYERS) }
            )
            TabItem(
                title = "Toppings",
                painter = painterResource(R.drawable.toppings_icon),
                isSelected = selectedTab == CakeCustomizationTab.TOPPINGS,
                onClick = { onTabSelected(CakeCustomizationTab.TOPPINGS) }
            )
            TabItem(
                title = "Text",
                painter = painterResource(R.drawable.text_icon),
                isSelected = selectedTab == CakeCustomizationTab.TEXT,
                onClick = { onTabSelected(CakeCustomizationTab.TEXT) }
            )
            TabItem(
                title = "Tags",
                painter = painterResource(R.drawable.tags_icon),
                isSelected = selectedTab == CakeCustomizationTab.TAGS,
                onClick = { onTabSelected(CakeCustomizationTab.TAGS) }
            )
        }
    }
}

@Composable
fun TabItem(
    title: String,
    painter: Painter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(
            painter = painter,
            contentDescription = title,
            tint = if (isSelected) Color(0xFF6366F1) else Color(0xFF9CA3AF),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            color = if (isSelected) Color(0xFF6366F1) else Color(0xFF9CA3AF),
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}