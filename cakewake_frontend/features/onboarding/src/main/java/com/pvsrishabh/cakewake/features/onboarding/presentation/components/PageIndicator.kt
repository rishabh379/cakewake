package com.pvsrishabh.cakewake.features.onboarding.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.pvsrishabh.cakewake.core.utils.Dimens.IndicatorSize
import com.pvsrishabh.cakewake.core.ui.theme.BlueGray
import com.pvsrishabh.cakewake.features.onboarding.R

@Composable
fun PageIndicator(
    modifier: Modifier = Modifier,
    pageSize: Int,
    selectedPage: Int,
    selectedColor: Color = colorResource(R.color.splash_background),
    unselectedColor: Color = BlueGray
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(15.dp)) {
        repeat(pageSize){page ->
            Box(modifier = Modifier.size(IndicatorSize).clip(CircleShape).background(
                color = if(page == selectedPage) selectedColor else unselectedColor
            ))
        }
    }
}