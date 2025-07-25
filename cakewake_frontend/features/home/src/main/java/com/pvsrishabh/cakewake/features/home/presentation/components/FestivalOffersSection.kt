package com.pvsrishabh.cakewake.features.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvsrishabh.cakewake.features.home.domain.model.SpecialOffer

@Composable
fun FestivalOffersSection(
    offers: List<SpecialOffer>,
    onOfferClick: (String) -> Unit
) {
    Column {
        SectionHeader(title = "CakeWake Festive", titleColor = Color(0xFFE91E63))

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            // Add static festival items since we don't have dynamic data
            item {
                FestivalOfferItem(
                    title = "Holi Special",
                    backgroundColor = Color(0xFFE91E63),
                    onClick = { onOfferClick("holi") }
                )
            }
            item {
                FestivalOfferItem(
                    title = "Diwali Special",
                    backgroundColor = Color(0xFFFFC107),
                    onClick = { onOfferClick("diwali") }
                )
            }
            item {
                FestivalOfferItem(
                    title = "Eid Special",
                    backgroundColor = Color(0xFF4CAF50),
                    onClick = { onOfferClick("eid") }
                )
            }
        }
    }
}

@Composable
fun FestivalOfferItem(
    title: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(90.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title.replace(" Special", ""),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Special",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}