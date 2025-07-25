package com.pvsrishabh.cakewake.app.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pvsrishabh.cakewake.app.splash.CakeWakeSplashScreen
import com.pvsrishabh.cakewake.app.navigation.NavGraph
import com.pvsrishabh.cakewake.core.ui.theme.CakewakeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Handle the splash screen transition
//        val splashScreen = installSplashScreen()

        // Set the splash screen to immediately dismiss once we're ready
//        splashScreen.setKeepOnScreenCondition { false }


        setContent {
            CakewakeTheme {

                val isSystemInDarkMode = isSystemInDarkTheme()
                val systemController = rememberSystemUiController()

                SideEffect {
                    systemController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = !isSystemInDarkMode
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background)
                ) {
                    if (viewModel.splashCondition) {
                        CakeWakeSplashScreen()
                    }else{
                        val startDestination = viewModel.startDestination
                        NavGraph(startDestination = startDestination)
                    }
                }
            }
        }
    }
}