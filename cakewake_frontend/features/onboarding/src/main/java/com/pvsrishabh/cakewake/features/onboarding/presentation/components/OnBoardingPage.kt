package com.pvsrishabh.cakewake.features.onboarding.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pvsrishabh.cakewake.core.utils.Dimens.ExtraSmallPadding2
import com.pvsrishabh.cakewake.core.utils.Dimens.MediumPadding1
import com.pvsrishabh.cakewake.features.onboarding.R
import com.pvsrishabh.cakewake.features.onboarding.presentation.Page

@Composable
fun OnBoardingPage(
    modifier: Modifier = Modifier,
    page: Page,
    pageIndex: Int
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
        ) {
            when (pageIndex) {
                0 -> {
                    // Welcome screen
                    Image(
                        painter = painterResource(id = page.images[0]),
                        contentDescription = null,
                        modifier = Modifier
                            .size(300.dp)
                            .align(Alignment.Center)
                    )
                    Image(
                        painter = painterResource(id = page.images[1]),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.TopStart)
                            .offset(x = (-35).dp, y = 10.dp)
                    )
                    Image(
                        painter = painterResource(id = page.images[2]),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = (45).dp, y = (-10).dp)
                    )
                }

                1 -> {
                    // Design screen
                    Image(
                        painter = painterResource(id = page.images[0]),
                        contentDescription = null,
                        modifier = Modifier
                            .size(300.dp)
                            .align(Alignment.Center)
                    )
                    Image(
                        painter = painterResource(id = page.images[1]),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.TopCenter)
                            .offset(y = 40.dp)
                    )
                    Image(
                        painter = painterResource(id = page.images[2]),
                        contentDescription = null,
                        modifier = Modifier
                            .size(170.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = (8).dp, y = 30.dp)
                    )
                    Image(
                        painter = painterResource(id = page.images[3]),
                        contentDescription = null,
                        modifier = Modifier
                            .size(180.dp)
                            .align(Alignment.TopStart)
                            .offset(x = (-25).dp, y = 44.dp)
                    )
                    Image(
                        painter = painterResource(id = page.images[4]),
                        contentDescription = null,
                        modifier = Modifier
                            .size(330.dp)
                            .align(Alignment.BottomStart)
                            .offset(x = (-60).dp, y = (16).dp)
                    )
                    Image(
                        painter = painterResource(id = page.images[5]),
                        contentDescription = null,
                        modifier = Modifier
                            .size(70.dp)
                            .align(Alignment.BottomCenter)
                            .offset(y = (-60).dp)
                    )
                    Image(
                        painter = painterResource(id = page.images[6]),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = (-50).dp, y = (-100).dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(MediumPadding1))
        Text(
            text = page.title,
            fontSize = 30.sp,
            modifier = Modifier.padding(horizontal = MediumPadding1),
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
            color = colorResource(id = R.color.display_small),
        )
        Spacer(modifier = Modifier.height(ExtraSmallPadding2))
        Text(
            text = page.description,
            modifier = Modifier.padding(horizontal = MediumPadding1),
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(id = R.color.text_medium),
        )
    }
}