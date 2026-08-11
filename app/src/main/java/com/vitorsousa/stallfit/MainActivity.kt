package com.vitorsousa.stallfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vitorsousa.stallfit.navigation.StallFitApp as StallFitAppRoot
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
                StallFitAppRoot()
            }
        }
    }
}
