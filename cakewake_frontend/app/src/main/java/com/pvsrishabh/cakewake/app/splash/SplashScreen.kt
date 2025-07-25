package com.pvsrishabh.cakewake.app.splash

import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.scale
import com.pvsrishabh.cakewake.R

@Composable
fun CakeWakeSplashScreen() {
    val backgroundBrush = repeatingImageBackgroundBrush(R.drawable.bg_img)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7974DB))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundBrush)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(35.dp)
                            .offset(y = (-30).dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.text_cake_wake),
                        contentDescription = "Text",
                        modifier = Modifier
                            .size(170.dp)
                            .offset(y = (15).dp)
                    )
                }
            }
        }
    }
}

@Composable
fun repeatingImageBackgroundBrush(@DrawableRes imageRes: Int): Brush {
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics

    val originalBitmap = BitmapFactory.decodeResource(context.resources, imageRes)

    // Calculate 1/9th of screen width and height
    val targetWidth = displayMetrics.widthPixels / 3   // width scaled for repeat (3x3 = 9)
    val targetHeight = displayMetrics.heightPixels / 3

    val scaledBitmap = originalBitmap.scale(targetWidth, targetHeight)

    val imageBitmap = scaledBitmap.asImageBitmap()

    return remember(imageBitmap) {
        ShaderBrush(
            ImageShader(imageBitmap, TileMode.Repeated, TileMode.Repeated)
        )
    }
}