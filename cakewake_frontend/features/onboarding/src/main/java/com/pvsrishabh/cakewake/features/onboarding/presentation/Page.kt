package com.pvsrishabh.cakewake.features.onboarding.presentation

import androidx.annotation.DrawableRes
import com.pvsrishabh.cakewake.features.onboarding.R

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val images: List<Int>
)

val pages = listOf(
    Page(
        title = "Welcome to the World of Custom Cakes!",
        description = "Design your dream cake or explore magical creations by people like you.",
        images = listOf(
            R.drawable.cake_center1,
            R.drawable.cake_top_left,
            R.drawable.cake_right_bottom
        )
    ),
    Page(
        title = "Design, Share, Celebrate",
        description = "Use our drag & drop editor to design cakes your way, or choose from trending ones.",
        images = listOf(
            R.drawable.cake_center2,
            R.drawable.heart_chocolate,
            R.drawable.whisk,
            R.drawable.piping_bag,
            R.drawable.sprinkles,
            R.drawable.dewberry,
            R.drawable.strawberry,
        )
    )
)