package com.vitorsousa.stallfit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val StallFitDarkColorScheme = darkColorScheme(
    primary = Volt,
    onPrimary = BackgroundDark,
    primaryContainer = VoltDim,
    onPrimaryContainer = BackgroundDark,
    secondary = ElectricBlue,
    onSecondary = BackgroundDark,
    tertiary = Amber,
    onTertiary = BackgroundDark,
    error = Coral,
    onError = BackgroundDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnBackgroundDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceMutedDark,
    outline = OutlineDark
)

/**
 * StällFit is designed dark-first: the app always renders in its dark
 * palette regardless of system theme, matching the high-contrast,
 * gym-usable UI described in the product spec.
 */
@Composable
fun StallFitTheme(content: @Composable () -> Unit) {
    val colorScheme = StallFitDarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        val activity = view.context as? android.app.Activity
        activity?.window?.let { window ->
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = StallFitTypography,
        content = content
    )
}
