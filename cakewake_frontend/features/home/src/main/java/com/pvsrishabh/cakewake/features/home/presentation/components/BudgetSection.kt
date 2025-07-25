package com.pvsrishabh.cakewake.features.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvsrishabh.cakewake.features.home.domain.model.BudgetRange

@Composable
fun BudgetSection(
    selectedBudget: BudgetRange?,
    onBudgetClick: (BudgetRange?) -> Unit
) {
    val budgetRanges = listOf(
        BudgetRange(0, 1000, "Under\n₹1000"),
        BudgetRange(1000, 5000, "Under\n₹5000"),
        BudgetRange(5000, 8000, "Under\n₹8000")
    )

    Column {
        SectionHeader(title = "Shop by budget", titleColor = Color(0xFF9C27B0))

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(budgetRanges) { budget ->
                BudgetItem(
                    budget = budget,
                    isSelected = selectedBudget == budget,
                    onClick = {
                        onBudgetClick(
                            if (selectedBudget == budget) null else budget
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun BudgetItem(
    budget: BudgetRange,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when (budget.max) {
        1000 -> Color(0xFF9C27B0)
        5000 -> Color(0xFF673AB7)
        8000 -> Color(0xFF3F51B5)
        else -> Color(0xFFFF6B9D)
    }

    Box(
        modifier = Modifier
            .width(110.dp)
            .height(90.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) backgroundColor.copy(alpha = 0.8f) else backgroundColor
            )
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) backgroundColor else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = budget.label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
    }
}