package com.vitorsousa.stallfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.vitorsousa.stallfit.navigation.StallFitApp as StallFitAppRoot
import com.vitorsousa.stallfit.ui.splash.SplashScreen
import com.vitorsousa.stallfit.ui.theme.StallFitTheme

/**
 * Single-activity entry point — the whole app is one Compose tree navigated by [StallFitAppRoot].
 * Edge-to-edge is intentionally not enabled here: [StallFitTheme] already paints the status and
 * navigation bars to match the dark background, which keeps the Scaffold/NavigationBar layout
 * simple and avoids needing manual WindowInsets handling throughout the UI.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StallFitTheme {
                var showSplash by remember { mutableStateOf(true) }
                Crossfade(targetState = showSplash, label = "splash") { splashVisible ->
                    if (splashVisible) {
                        SplashScreen(onFinished = { showSplash = false })
                    } else {
                        StallFitAppRoot()
                    }
                }
            }
        }
    }
}
