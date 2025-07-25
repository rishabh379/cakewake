package com.pvsrishabh.cakewake.features.cake_customization.presentation

import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeLayer
import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeViewMode

sealed class CakeCustomizationEvent {
    data class ChangeBase(val layer: CakeLayer) : CakeCustomizationEvent()
    data class ChangeSide(val layer: CakeLayer) : CakeCustomizationEvent()
    data class ChangeTop(val layer: CakeLayer) : CakeCustomizationEvent()
    data class AddIngredient(val layer: CakeLayer) : CakeCustomizationEvent()
    data class RemoveIngredient(val layerId: String) : CakeCustomizationEvent()
    data class SelectLayer(val layerId: String) : CakeCustomizationEvent()
    data class MoveLayer(val layerId: String, val dx: Float, val dy: Float) : CakeCustomizationEvent()
    data class AddText(val text: String) : CakeCustomizationEvent()
    data class RemoveText(val textId: String) : CakeCustomizationEvent()
    data class SelectText(val textId: String) : CakeCustomizationEvent()
    data class MoveText(val textId: String, val dx: Float, val dy: Float) : CakeCustomizationEvent()
    data class ChangeViewMode(val viewMode: CakeViewMode) : CakeCustomizationEvent()
    data class SelectTab(val tab: CakeCustomizationTab) : CakeCustomizationEvent()
    data class AddTag(val tag: String) : CakeCustomizationEvent()
    data class RemoveTag(val tag: String) : CakeCustomizationEvent()
    object Undo : CakeCustomizationEvent()
    object Redo : CakeCustomizationEvent()
    data class SelectBaseCategory(val category: String?) : CakeCustomizationEvent()
    data class SelectSideCategory(val category: String?) : CakeCustomizationEvent()
    data class SelectTopCategory(val category: String?) : CakeCustomizationEvent()
}

enum class CakeCustomizationTab {
    BASE, LAYERS, TOPPINGS, TEXT, TAGS
}