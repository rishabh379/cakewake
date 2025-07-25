package com.pvsrishabh.cakewake.features.cake_customization.presentation

import androidx.lifecycle.ViewModel
import com.pvsrishabh.cakewake.features.cake_customization.domain.model.*
import com.pvsrishabh.cakewake.features.cake_customization.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class CakeCustomizationViewModel : ViewModel() {
    private val undoStack = mutableListOf<CakeCustomizationUiState>()
    private val redoStack = mutableListOf<CakeCustomizationUiState>()

    private fun baseLayers() = Constants.BASE_LAYERS.mapIndexed { idx, item ->
        CakeLayer(id = "base$idx", type = CakeLayerType.BASE, imageResourceId = item.resId, categories = item.categories)
    }
    private fun sideLayers() = Constants.SIDE_LAYERS.mapIndexed { idx, item ->
        CakeLayer(id = "side$idx", type = CakeLayerType.SIDE, imageResourceId = item.resId, categories = item.categories)
    }
    private fun topLayers() = Constants.TOP_LAYERS.mapIndexed { idx, item ->
        CakeLayer(id = "top$idx", type = CakeLayerType.TOP, imageResourceId = item.resId, categories = item.categories)
    }
    private fun ingredientLayers() = Constants.INGREDIENTS.mapIndexed { idx, item ->
        CakeLayer(id = "ing$idx", type = CakeLayerType.INGREDIENT, imageResourceId = item.resId, categories = item.categories)
    }

    private fun initialCake(): CakeCustomization {
        return CakeCustomization(
            baseLayer = baseLayers().firstOrNull() ?: CakeLayer(type = CakeLayerType.BASE, imageResourceId = 0),
            sideLayer = sideLayers().firstOrNull() ?: CakeLayer(type = CakeLayerType.SIDE, imageResourceId = 0),
            topLayer = topLayers().firstOrNull() ?: CakeLayer(type = CakeLayerType.TOP, imageResourceId = 0)
        )
    }

    private fun allCategories(layers: List<CakeLayer>): List<String> =
        layers.flatMap { it.categories }.distinct()

    private val allBaseLayers = baseLayers()
    private val allSideLayers = sideLayers()
    private val allTopLayers = topLayers()

    private val _uiState = MutableStateFlow(
        CakeCustomizationUiState(
            cake = initialCake(),
            availableBases = allBaseLayers,
            availableSides = allSideLayers,
            availableTops = allTopLayers,
            availableIngredients = ingredientLayers(),
            availableTags = listOf("Happy Birthday", "Congratulations", "Anniversary", "Celebration"),
            selectedTab = CakeCustomizationTab.BASE,
            baseCategories = allCategories(allBaseLayers),
            sideCategories = allCategories(allSideLayers),
            topCategories = allCategories(allTopLayers)
        )
    )

    val uiState: StateFlow<CakeCustomizationUiState> = _uiState

    fun onEvent(event: CakeCustomizationEvent) {
        when (event) {
            is CakeCustomizationEvent.ChangeBase -> {
                saveToUndo()
                _uiState.value = _uiState.value.copy(
                    cake = _uiState.value.cake.copy(baseLayer = event.layer)
                )
                clearRedoStack()
            }
            is CakeCustomizationEvent.ChangeSide -> {
                saveToUndo()
                _uiState.value = _uiState.value.copy(
                    cake = _uiState.value.cake.copy(sideLayer = event.layer)
                )
                clearRedoStack()
            }
            is CakeCustomizationEvent.ChangeTop -> {
                saveToUndo()
                _uiState.value = _uiState.value.copy(
                    cake = _uiState.value.cake.copy(topLayer = event.layer)
                )
                clearRedoStack()
            }
            is CakeCustomizationEvent.AddIngredient -> {
                saveToUndo()
                val newIngredient = event.layer.copy(
                    id = UUID.randomUUID().toString(),
                    position = SerializablePointF(0f, 0f)
                )
                _uiState.value = _uiState.value.copy(
                    cake = _uiState.value.cake.copy(
                        ingredients = _uiState.value.cake.ingredients + newIngredient
                    )
                )
                clearRedoStack()
            }
            is CakeCustomizationEvent.RemoveIngredient -> {
                saveToUndo()
                _uiState.value = _uiState.value.copy(
                    cake = _uiState.value.cake.copy(
                        ingredients = _uiState.value.cake.ingredients.filterNot { it.id == event.layerId }
                    ),
                    selectedLayerId = null
                )
                clearRedoStack()
            }
            is CakeCustomizationEvent.SelectLayer -> {
                _uiState.value = _uiState.value.copy(selectedLayerId = event.layerId, selectedTextId = null)
            }
            is CakeCustomizationEvent.MoveLayer -> {
                _uiState.value = _uiState.value.copy(
                    cake = _uiState.value.cake.copy(
                        ingredients = _uiState.value.cake.ingredients.map {
                            if (it.id == event.layerId) it.copy(
                                position = SerializablePointF(
                                    it.position.x + event.dx,
                                    it.position.y + event.dy
                                )
                            ) else it
                        }
                    )
                )
            }
            is CakeCustomizationEvent.AddText -> {
                saveToUndo()
                val newText = CakeText(
                    id = UUID.randomUUID().toString(),
                    text = event.text,
                    position = SerializablePointF(0f, 0f)
                )
                _uiState.value = _uiState.value.copy(
                    cake = _uiState.value.cake.copy(
                        textElements = _uiState.value.cake.textElements + newText
                    ),
                    selectedTextId = newText.id,
                    selectedLayerId = null
                )
                clearRedoStack()
            }
            is CakeCustomizationEvent.RemoveText -> {
                saveToUndo()
                _uiState.value = _uiState.value.copy(
                    cake = _uiState.value.cake.copy(
                        textElements = _uiState.value.cake.textElements.filterNot { it.id == event.textId }
                    ),
                    selectedTextId = null
                )
                clearRedoStack()
            }
            is CakeCustomizationEvent.SelectText -> {
                _uiState.value = _uiState.value.copy(selectedTextId = event.textId, selectedLayerId = null)
            }
            is CakeCustomizationEvent.MoveText -> {
                _uiState.value = _uiState.value.copy(
                    cake = _uiState.value.cake.copy(
                        textElements = _uiState.value.cake.textElements.map {
                            if (it.id == event.textId) it.copy(
                                position = SerializablePointF(
                                    it.position.x + event.dx,
                                    it.position.y + event.dy
                                )
                            ) else it
                        }
                    )
                )
            }
            is CakeCustomizationEvent.ChangeViewMode -> {
                _uiState.value = _uiState.value.copy(
                    cake = _uiState.value.cake.copy(viewMode = event.viewMode)
                )
            }
            is CakeCustomizationEvent.SelectTab -> {
                _uiState.value = _uiState.value.copy(
                    selectedTab = event.tab,
                    selectedLayerId = null,
                    selectedTextId = null
                )
            }
            is CakeCustomizationEvent.AddTag -> {
                saveToUndo()
                val newText = CakeText(
                    id = UUID.randomUUID().toString(),
                    text = event.tag,
                    position = SerializablePointF(0f, 0f)
                )
                _uiState.value = _uiState.value.copy(
                    cake = _uiState.value.cake.copy(
                        textElements = _uiState.value.cake.textElements + newText
                    )
                )
                clearRedoStack()
            }
            is CakeCustomizationEvent.RemoveTag -> {
                saveToUndo()
                _uiState.value = _uiState.value.copy(
                    cake = _uiState.value.cake.copy(
                        textElements = _uiState.value.cake.textElements.filterNot { it.text == event.tag }
                    )
                )
                clearRedoStack()
            }
            is CakeCustomizationEvent.Undo -> {
                if (undoStack.isNotEmpty()) {
                    redoStack.add(_uiState.value.copy())
                    _uiState.value = undoStack.removeAt(undoStack.lastIndex)
                }
            }
            is CakeCustomizationEvent.Redo -> {
                if (redoStack.isNotEmpty()) {
                    undoStack.add(_uiState.value.copy())
                    _uiState.value = redoStack.removeAt(redoStack.lastIndex)
                }
            }
            is CakeCustomizationEvent.SelectBaseCategory -> {
                _uiState.value = _uiState.value.copy(
                    selectedBaseCategory = event.category,
                    availableBases = if (event.category == null)
                        allBaseLayers
                    else
                        allBaseLayers.filter { it.categories.contains(event.category) }
                )
            }
            is CakeCustomizationEvent.SelectSideCategory -> {
                _uiState.value = _uiState.value.copy(
                    selectedSideCategory = event.category,
                    availableSides = if (event.category == null)
                        allSideLayers
                    else
                        allSideLayers.filter { it.categories.contains(event.category) }
                )
            }
            is CakeCustomizationEvent.SelectTopCategory -> {
                _uiState.value = _uiState.value.copy(
                    selectedTopCategory = event.category,
                    availableTops = if (event.category == null)
                        allTopLayers
                    else
                        allTopLayers.filter { it.categories.contains(event.category) }
                )
            }
        }
        updateUndoRedoState()
    }

    private fun saveToUndo() {
        undoStack.add(_uiState.value.copy())
        if (undoStack.size > 50) undoStack.removeAt(0)
    }

    private fun clearRedoStack() {
        redoStack.clear()
    }

    private fun updateUndoRedoState() {
        _uiState.value = _uiState.value.copy(
            canUndo = undoStack.isNotEmpty(),
            canRedo = redoStack.isNotEmpty()
        )
    }
}