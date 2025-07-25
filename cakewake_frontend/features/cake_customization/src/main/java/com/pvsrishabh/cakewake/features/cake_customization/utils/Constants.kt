package com.pvsrishabh.cakewake.features.cake_customization.utils

import com.pvsrishabh.cakewake.features.cake_customization.R
import com.pvsrishabh.cakewake.features.cake_customization.domain.model.CakeLayerItem

object Constants {
    val BASE_LAYERS = listOf(
        CakeLayerItem(R.drawable.base_sponge, listOf("Sponge", "Vanilla")),
        CakeLayerItem(R.drawable.base_heart, listOf("Heart", "Chocolate")),
        CakeLayerItem(R.drawable.base_chocolate, listOf("Chocolate")),
        CakeLayerItem(R.drawable.base_vanilla, listOf("Vanilla")),
        CakeLayerItem(R.drawable.base_red_velvet, listOf("Red Velvet"))
    )

    val SIDE_LAYERS = listOf(
        CakeLayerItem(R.drawable.side_frost, listOf("Frosting", "Vanilla")),
        CakeLayerItem(R.drawable.side_heart, listOf("Heart", "Cream")),
        CakeLayerItem(R.drawable.side_vanilla, listOf("Vanilla")),
        CakeLayerItem(R.drawable.side_cream, listOf("Cream")),
        CakeLayerItem(R.drawable.side_fondant, listOf("Fondant")),
        CakeLayerItem(R.drawable.side_buttercream, listOf("Buttercream"))
    )

    val TOP_LAYERS = listOf(
        CakeLayerItem(R.drawable.top_berries, listOf("Berries", "Fruit")),
        CakeLayerItem(R.drawable.top_cherries, listOf("Cherries", "Fruit")),
        CakeLayerItem(R.drawable.top_choco, listOf("Chocolate"))
    )

    val INGREDIENTS = listOf(
        CakeLayerItem(R.drawable.topping_strawberry, listOf("Strawberry", "Fruit")),
        CakeLayerItem(R.drawable.topping_dewberry, listOf("Dewberry", "Fruit")),
        CakeLayerItem(R.drawable.topping_chocolate_chips, listOf("Chocolate")),
        CakeLayerItem(R.drawable.topping_sprinkles, listOf("Sprinkles")),
        CakeLayerItem(R.drawable.topping_nuts, listOf("Nuts"))
    )
}