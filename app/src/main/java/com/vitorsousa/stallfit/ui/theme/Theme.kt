package com.vitorsousa.stallfit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Every role below is explicitly assigned from the brand palette. Leaving any role unset
// makes Compose fall back to Material's generic baseline (purple) tokens, which is what was
// leaking into components that read them by default — e.g. NavigationBarItem's selected
// indicator pill reads secondaryContainer, and AlertDialog/DropdownMenu read the
// surfaceContainer* tiers.
private val StallFitDarkColorScheme = darkColorScheme(
    primary = VoltNeon,
    onPrimary = ObsidianBackground,
    primaryContainer = PrimaryContainerTint,
    onPrimaryContainer = VoltNeon,
    inversePrimary = VoltNeonVariant,
    secondary = StatusSuccess,
    onSecondary = ObsidianBackground,
    secondaryContainer = SecondaryContainerTint,
    onSecondaryContainer = StatusSuccess,
    tertiary = StatusWater,
    onTertiary = TextPrimary,
    tertiaryContainer = TertiaryContainerTint,
    onTertiaryContainer = StatusWater,
    error = StatusWarning,
    onError = TextPrimary,
    errorContainer = ErrorContainerTint,
    onErrorContainer = StatusWarning,
    background = ObsidianBackground,
    onBackground = TextPrimary,
    surface = DarkSlateCard,
    onSurface = TextPrimary,
    surfaceVariant = CharcoalInput,
    onSurfaceVariant = TextSecondary,
    surfaceTint = Color.Transparent,
    inverseSurface = TextPrimary,
    inverseOnSurface = ObsidianBackground,
    outline = SteelBorder,
    outlineVariant = OutlineVariant,
    // Tonal ladder from darkest (background) to lightest (menus/dialogs), used to separate
    // elevated surfaces without relying on surfaceTint or drop shadows.
    surfaceDim = ObsidianBackground,
    surfaceBright = SurfaceContainerHighest,
    surfaceContainerLowest = ObsidianBackground,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = DarkSlateCard,
    surfaceContainerHigh = CharcoalInput,
    surfaceContainerHighest = SurfaceContainerHighest
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
