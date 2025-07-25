package com.pvsrishabh.cakewake.features.cake_customization.data.local.database

import androidx.room.TypeConverter
import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeLayer
import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeText
import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeViewMode
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CakeCustomizationJson {
    val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
    }
}

class Converters {
    @TypeConverter
    fun fromCakeLayerList(value: String): List<CakeLayer> =
        CakeCustomizationJson.json.decodeFromString(value)

    @TypeConverter
    fun toCakeLayerList(value: List<CakeLayer>): String =
        CakeCustomizationJson.json.encodeToString(value)

    @TypeConverter
    fun fromCakeTextList(value: String): List<CakeText> =
        CakeCustomizationJson.json.decodeFromString(value)

    @TypeConverter
    fun toCakeTextList(value: List<CakeText>): String =
        CakeCustomizationJson.json.encodeToString(value)

    @TypeConverter
    fun fromCakeLayer(value: String): CakeLayer =
        CakeCustomizationJson.json.decodeFromString(value)

    @TypeConverter
    fun toCakeLayer(value: CakeLayer): String =
        CakeCustomizationJson.json.encodeToString(value)

    @TypeConverter
    fun fromCakeViewMode(value: String): CakeViewMode =
        enumValueOf(value)

    @TypeConverter
    fun toCakeViewMode(value: CakeViewMode): String =
        value.name
}

