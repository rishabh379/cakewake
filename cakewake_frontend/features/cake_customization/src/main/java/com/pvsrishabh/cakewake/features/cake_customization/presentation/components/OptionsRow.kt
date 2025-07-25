package com.pvsrishabh.cakewake.features.cake_customization.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeLayer
import com.pvsrishabh.cakewake.features.cake_customization.presentation.CakeCustomizationTab
import com.pvsrishabh.cakewake.features.cake_customization.presentation.CakeCustomizationUiState

@Composable
fun OptionsRow(
    state: CakeCustomizationUiState,
    onBaseSelected: (CakeLayer) -> Unit,
    onSideSelected: (CakeLayer) -> Unit,
    onTopSelected: (CakeLayer) -> Unit,
    onIngredientAdded: (CakeLayer) -> Unit,
    onTextAdded: (String) -> Unit,
    onTagAdded: (String) -> Unit,
    onBaseCategorySelected: (String?) -> Unit,
    onSideCategorySelected: (String?) -> Unit,
    onTopCategorySelected: (String?) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            when (state.selectedTab) {
                CakeCustomizationTab.BASE -> {
                    Text(
                        text = "Choose Base",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    // Category chips row inside options row
                    CategoryChipsRow(
                        categories = state.baseCategories,
                        selectedCategory = state.selectedBaseCategory,
                        onCategorySelected = onBaseCategorySelected
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Filtered items
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            state.availableBases.filter {
                                state.selectedBaseCategory == null || it.categories.contains(state.selectedBaseCategory)
                            }
                        ) { base ->
                            OptionItem(
                                layer = base,
                                isSelected = state.cake.baseLayer.id == base.id,
                                onClick = { onBaseSelected(base) }
                            )
                        }
                    }
                }

                CakeCustomizationTab.LAYERS -> {
                    Text(
                        text = "Choose Side Layer",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    // Category chips row inside options row
                    CategoryChipsRow(
                        categories = state.sideCategories,
                        selectedCategory = state.selectedSideCategory,
                        onCategorySelected = onSideCategorySelected
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Filtered items
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            state.availableSides.filter {
                                state.selectedSideCategory == null || it.categories.contains(state.selectedSideCategory)
                            }
                        ) { side ->
                            OptionItem(
                                layer = side,
                                isSelected = state.cake.sideLayer.id == side.id,
                                onClick = { onSideSelected(side) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Choose Top Layer",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    // Category chips row inside options row
                    CategoryChipsRow(
                        categories = state.topCategories,
                        selectedCategory = state.selectedTopCategory,
                        onCategorySelected = onTopCategorySelected
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Filtered items
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            state.availableTops.filter {
                                state.selectedTopCategory == null || it.categories.contains(state.selectedTopCategory)
                            }
                        ) { top ->
                            OptionItem(
                                layer = top,
                                isSelected = state.cake.topLayer.id == top.id,
                                onClick = { onTopSelected(top) }
                            )
                        }
                    }
                }

                CakeCustomizationTab.TOPPINGS -> {
                    Text(
                        text = "Add Toppings",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.availableIngredients) { ingredient ->
                            OptionItem(
                                layer = ingredient,
                                isSelected = false,
                                onClick = { onIngredientAdded(ingredient) }
                            )
                        }
                    }
                }

                CakeCustomizationTab.TEXT -> {
                    var textInput by remember { mutableStateOf(TextFieldValue("")) }

                    Text(
                        text = "Add Custom Text",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = textInput,
                            onValueChange = { textInput = it },
                            label = { Text("Enter text") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (textInput.text.isNotBlank()) {
                                    onTextAdded(textInput.text)
                                    textInput = TextFieldValue("")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6366F1)
                            )
                        ) {
                            Text("Add", color = Color.White)
                        }
                    }
                }

                CakeCustomizationTab.TAGS -> {
                    Text(
                        text = "Add Predefined Tags",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.availableTags) { tag ->
                            TagItem(
                                tag = tag,
                                onClick = { onTagAdded(tag) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OptionItem(
    layer: CakeLayer,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(
                if (isSelected) Color(0xFFDDD6FE) else Color(0xFFF9FAFB),
                RoundedCornerShape(12.dp)
            )
            .border(
                if (isSelected) 2.dp else 1.dp,
                if (isSelected) Color(0xFF8B5CF6) else Color(0xFFE5E7EB),
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (layer.imageResourceId != 0) {
            Icon(
                painter = painterResource(id = layer.imageResourceId),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun TagItem(
    tag: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                Color(0xFFF3F4F6),
                RoundedCornerShape(20.dp)
            )
            .border(
                1.dp,
                Color(0xFFE5E7EB),
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = tag,
            fontSize = 14.sp,
            color = Color(0xFF374151),
            fontWeight = FontWeight.Medium
        )
    }
}