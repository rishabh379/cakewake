package com.pvsrishabh.cakewake.features.cake_customization.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeLayer
import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeViewMode
import com.pvsrishabh.cakewake.features.cake_customization.presentation.components.BottomTabBar
import com.pvsrishabh.cakewake.features.cake_customization.presentation.components.CustomTopAppBar
import com.pvsrishabh.cakewake.features.cake_customization.presentation.components.OptionsRow
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CakeCustomizationScreen(
    viewModel: CakeCustomizationViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onBuyClick: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CustomTopAppBar(
                viewModel,
                onNavigateBack,
                onBuyClick
            )
        },
        bottomBar = {
            BottomTabBar(
                selectedTab = state.selectedTab,
                onTabSelected = { viewModel.onEvent(CakeCustomizationEvent.SelectTab(it)) }
            )
        },
        containerColor = Color(0xFFF8F9FA),
        modifier = Modifier.navigationBarsPadding()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // View Mode Toggle
            ViewModeToggle(
                currentViewMode = state.viewMode,
                onViewModeChanged = { viewModel.onEvent(CakeCustomizationEvent.ChangeViewMode(it)) }
            )

            // Canvas area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                CakeCanvas(
                    state = state,
                    onLayerSelected = { viewModel.onEvent(CakeCustomizationEvent.SelectLayer(it)) },
                    onLayerMoved = { id, dx, dy ->
                        viewModel.onEvent(
                            CakeCustomizationEvent.MoveLayer(
                                id,
                                dx,
                                dy
                            )
                        )
                    },
                    onTextSelected = { viewModel.onEvent(CakeCustomizationEvent.SelectText(it)) },
                    onTextMoved = { id, dx, dy ->
                        viewModel.onEvent(
                            CakeCustomizationEvent.MoveText(
                                id,
                                dx,
                                dy
                            )
                        )
                    }
                )
            }

            // Options Row based on selected tab
            OptionsRow(
                state = state,
                onBaseSelected = { viewModel.onEvent(CakeCustomizationEvent.ChangeBase(it)) },
                onSideSelected = { viewModel.onEvent(CakeCustomizationEvent.ChangeSide(it)) },
                onTopSelected = { viewModel.onEvent(CakeCustomizationEvent.ChangeTop(it)) },
                onIngredientAdded = { viewModel.onEvent(CakeCustomizationEvent.AddIngredient(it)) },
                onTextAdded = { viewModel.onEvent(CakeCustomizationEvent.AddText(it)) },
                onTagAdded = { viewModel.onEvent(CakeCustomizationEvent.AddTag(it)) },
                onBaseCategorySelected = { category ->
                    viewModel.onEvent(CakeCustomizationEvent.SelectBaseCategory(category))
                },
                onSideCategorySelected = { category ->
                    viewModel.onEvent(CakeCustomizationEvent.SelectSideCategory(category))
                },
                onTopCategorySelected = { category ->
                    viewModel.onEvent(CakeCustomizationEvent.SelectTopCategory(category))
                }
            )
        }
    }
}

@Composable
fun ViewModeToggle(
    currentViewMode: CakeViewMode,
    onViewModeChanged: (CakeViewMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (currentViewMode == CakeViewMode.TOP) "Top View" else "Side View",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF6B7280),
            modifier = Modifier.clickable {
                onViewModeChanged(
                    if (currentViewMode == CakeViewMode.TOP) CakeViewMode.SIDE else CakeViewMode.TOP
                )
            }
        )
    }
}

@Composable
fun CakeCanvas(
    state: CakeCustomizationUiState,
    onLayerSelected: (String) -> Unit,
    onLayerMoved: (String, Float, Float) -> Unit,
    onTextSelected: (String) -> Unit,
    onTextMoved: (String, Float, Float) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Draw cake layers based on view mode
        if (state.viewMode == CakeViewMode.TOP) {
            TopViewCake(
                state = state,
                onLayerSelected = onLayerSelected,
                onLayerMoved = onLayerMoved,
                onTextSelected = onTextSelected,
                onTextMoved = onTextMoved
            )
        } else {
            SideViewCake(
                state = state,
                onLayerSelected = onLayerSelected,
                onLayerMoved = onLayerMoved,
                onTextSelected = onTextSelected,
                onTextMoved = onTextMoved
            )
        }
    }
}

@Composable
fun TopViewCake(
    state: CakeCustomizationUiState,
    onLayerSelected: (String) -> Unit,
    onLayerMoved: (String, Float, Float) -> Unit,
    onTextSelected: (String) -> Unit,
    onTextMoved: (String, Float, Float) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(280.dp)
    ) {
        // Base layer (largest circle)
        CakeLayerComposable(
            layer = state.cake.baseLayer,
            isSelected = state.selectedLayerId == state.cake.baseLayer.id,
            onSelect = { onLayerSelected(state.cake.baseLayer.id) },
            size = 222.dp
        )

        // Side layer (medium circle)
        CakeLayerComposable(
            layer = state.cake.sideLayer,
            isSelected = state.selectedLayerId == state.cake.sideLayer.id,
            onSelect = { onLayerSelected(state.cake.sideLayer.id) },
            size = 232.dp
        )

        // Top layer (smallest circle)
        CakeLayerComposable(
            modifier = Modifier.padding(bottom = 42.dp),
            layer = state.cake.topLayer,
            isSelected = state.selectedLayerId == state.cake.topLayer.id,
            onSelect = { onLayerSelected(state.cake.topLayer.id) },
            size = 184.dp
        )

        // Draw ingredients (draggable)
        state.cake.ingredients.forEach { ingredient ->
            var offset by remember {
                mutableStateOf(
                    IntOffset(
                        ingredient.position.x.roundToInt(),
                        ingredient.position.y.roundToInt()
                    )
                )
            }
            Box(
                modifier = Modifier
                    .offset { offset }
                    .size(48.dp)
                    .background(
//                        if (state.selectedLayerId == ingredient.id) Color(0xFFDDD6FE) else Color.White,
                        color = Color.Transparent,
                        CircleShape
                    )
//                    .border(
//                        2.dp,
//                        if (state.selectedLayerId == ingredient.id) Color(0xFF8B5CF6) else Color(0xFFE5E7EB),
//                        CircleShape
//                    )
                    .pointerInput(ingredient.id) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offset += IntOffset(
                                dragAmount.x.roundToInt(),
                                dragAmount.y.roundToInt()
                            )
                            onLayerMoved(ingredient.id, dragAmount.x, dragAmount.y)
                        }
                    }
                    .clickable { onLayerSelected(ingredient.id) },
                contentAlignment = Alignment.Center
            ) {
                if (ingredient.imageResourceId != 0) {
                    Icon(
                        painter = painterResource(id = ingredient.imageResourceId),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color.Unspecified
                    )
                }
            }
        }

        // Draw text elements (draggable)
        state.cake.textElements.forEach { textElem ->
            var offset by remember {
                mutableStateOf(
                    IntOffset(
                        textElem.position.x.roundToInt(),
                        textElem.position.y.roundToInt()
                    )
                )
            }
            Box(
                modifier = Modifier
                    .offset { offset }
                    .background(
                        if (state.selectedTextId == textElem.id) Color(0xFFFEF3C7) else Color.Transparent,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
                    .pointerInput(textElem.id) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offset += IntOffset(
                                dragAmount.x.roundToInt(),
                                dragAmount.y.roundToInt()
                            )
                            onTextMoved(textElem.id, dragAmount.x, dragAmount.y)
                        }
                    }
                    .clickable { onTextSelected(textElem.id) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = textElem.text,
                    color = Color(0xFF374151),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun SideViewCake(
    state: CakeCustomizationUiState,
    onLayerSelected: (String) -> Unit,
    onLayerMoved: (String, Float, Float) -> Unit,
    onTextSelected: (String) -> Unit,
    onTextMoved: (String, Float, Float) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(280.dp, 200.dp)
    ) {
        // Side view representation - rectangular layers stacked
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            // Top layer
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(30.dp)
                    .background(
                        if (state.selectedLayerId == state.cake.topLayer.id) Color(0xFFDDD6FE) else Color(
                            0xFFF3F4F6
                        ),
                        RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    )
                    .border(
                        if (state.selectedLayerId == state.cake.topLayer.id) 2.dp else 1.dp,
                        if (state.selectedLayerId == state.cake.topLayer.id) Color(0xFF8B5CF6) else Color(
                            0xFFE5E7EB
                        ),
                        RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    )
                    .clickable { onLayerSelected(state.cake.topLayer.id) }
            )

            // Side layer
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(40.dp)
                    .background(
                        if (state.selectedLayerId == state.cake.sideLayer.id) Color(0xFFDDD6FE) else Color(
                            0xFFF9FAFB
                        ),
                        RoundedCornerShape(0.dp)
                    )
                    .border(
                        if (state.selectedLayerId == state.cake.sideLayer.id) 2.dp else 1.dp,
                        if (state.selectedLayerId == state.cake.sideLayer.id) Color(0xFF8B5CF6) else Color(
                            0xFFE5E7EB
                        )
                    )
                    .clickable { onLayerSelected(state.cake.sideLayer.id) }
            )

            // Base layer
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(50.dp)
                    .background(
                        if (state.selectedLayerId == state.cake.baseLayer.id) Color(0xFFDDD6FE) else Color(
                            0xFFF3F4F6
                        ),
                        RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                    )
                    .border(
                        if (state.selectedLayerId == state.cake.baseLayer.id) 2.dp else 1.dp,
                        if (state.selectedLayerId == state.cake.baseLayer.id) Color(0xFF8B5CF6) else Color(
                            0xFFE5E7EB
                        ),
                        RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                    )
                    .clickable { onLayerSelected(state.cake.baseLayer.id) }
            )
        }

        // Side view ingredients and text (simplified positioning)
        state.cake.ingredients.forEach { ingredient ->
            var offset by remember {
                mutableStateOf(
                    IntOffset(
                        ingredient.position.x.roundToInt(),
                        ingredient.position.y.roundToInt()
                    )
                )
            }
            Box(
                modifier = Modifier
                    .offset { offset }
                    .size(32.dp)
                    .background(
                        if (state.selectedLayerId == ingredient.id) Color(0xFFDDD6FE) else Color.White,
                        CircleShape
                    )
                    .border(
                        1.dp,
                        if (state.selectedLayerId == ingredient.id) Color(0xFF8B5CF6) else Color(
                            0xFFE5E7EB
                        ),
                        CircleShape
                    )
                    .pointerInput(ingredient.id) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offset += IntOffset(
                                dragAmount.x.roundToInt(),
                                dragAmount.y.roundToInt()
                            )
                            onLayerMoved(ingredient.id, dragAmount.x, dragAmount.y)
                        }
                    }
                    .clickable { onLayerSelected(ingredient.id) },
                contentAlignment = Alignment.Center
            ) {
                if (ingredient.imageResourceId != 0) {
                    Icon(
                        painter = painterResource(id = ingredient.imageResourceId),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.Unspecified
                    )
                }
            }
        }

        // Side view text elements
        state.cake.textElements.forEach { textElem ->
            var offset by remember {
                mutableStateOf(
                    IntOffset(
                        textElem.position.x.roundToInt(),
                        textElem.position.y.roundToInt()
                    )
                )
            }
            Box(
                modifier = Modifier
                    .offset { offset }
                    .background(
                        if (state.selectedTextId == textElem.id) Color(0xFFFEF3C7) else Color.Transparent,
                        RoundedCornerShape(6.dp)
                    )
                    .padding(6.dp)
                    .pointerInput(textElem.id) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offset += IntOffset(
                                dragAmount.x.roundToInt(),
                                dragAmount.y.roundToInt()
                            )
                            onTextMoved(textElem.id, dragAmount.x, dragAmount.y)
                        }
                    }
                    .clickable { onTextSelected(textElem.id) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = textElem.text,
                    color = Color(0xFF374151),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun CakeLayerComposable(
    modifier: Modifier = Modifier,
    layer: CakeLayer,
    isSelected: Boolean,
    onSelect: () -> Unit,
    size: androidx.compose.ui.unit.Dp
) {
    Box(
        modifier = modifier
            .size(size)
            .background(
//                if (isSelected) Color(0xFFDDD6FE) else Color(0xFFF3F4F6),
                color = Color.Transparent,
                CircleShape
            )
//            .border(
//                if (isSelected) 3.dp else 1.dp,
//                if (isSelected) Color(0xFF8B5CF6) else Color(0xFFE5E7EB),
//                CircleShape
//            )
            .clickable { onSelect() },
        contentAlignment = Alignment.Center
    ) {
        if (layer.imageResourceId != 0) {
            Icon(
                painter = painterResource(id = layer.imageResourceId),
                contentDescription = null,
                modifier = Modifier.size(size * 0.8f),
                tint = Color.Unspecified
            )
        }
    }
}