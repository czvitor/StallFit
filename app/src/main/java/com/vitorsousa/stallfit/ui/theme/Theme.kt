package com.vitorsousa.stallfit.ui.theme

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
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

// Light counterpart of [StallFitDarkColorScheme], following the same "every role explicit"
// discipline. Surfaces stay off-white/white per the design spec's AAA-contrast requirement;
// accent roles reuse the AAA-safe deep tones from Color.kt rather than the literal dark-theme
// hexes, which fail badly as text/icon color against a light background.
private val StallFitLightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Color.White,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightPrimary,
    inversePrimary = VoltNeon,
    secondary = LightStatusSuccess,
    onSecondary = Color.White,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightStatusSuccess,
    tertiary = LightStatusWater,
    onTertiary = Color.White,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightStatusWater,
    error = LightStatusWarning,
    onError = Color.White,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightStatusWarning,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurfaceCard,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceDim,
    onSurfaceVariant = LightTextSecondary,
    surfaceTint = Color.Transparent,
    inverseSurface = ObsidianBackground,
    inverseOnSurface = TextPrimary,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    // Only three surface tiers exist in light mode (off-white background, dim, and pure-white
    // cards) — there's no headroom above white for a separate "elevated menus/dialogs" tier,
    // so those roles stay pinned to the card tier, matching the spec's "modals are pure white" rule.
    surfaceDim = LightSurfaceDim,
    surfaceBright = LightSurfaceCard,
    surfaceContainerLowest = LightBackground,
    surfaceContainerLow = LightSurfaceDim,
    surfaceContainer = LightSurfaceCard,
    surfaceContainerHigh = LightSurfaceCard,
    surfaceContainerHighest = LightSurfaceCard
)

/**
 * StällFit is dark-first: [isDarkTheme] defaults to `true` and the app only shows light when the
 * user explicitly opts in via the header toggle ([ThemeViewModel] persists that choice) — the OS
 * `prefers-color-scheme` setting is intentionally not consulted. Colors cross-fade over 300ms so
 * switching never causes a hard flash.
 */
@Composable
fun StallFitTheme(isDarkTheme: Boolean = true, content: @Composable () -> Unit) {
    val colorScheme = if (isDarkTheme) StallFitDarkColorScheme else StallFitLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        val activity = view.context as? android.app.Activity
        activity?.window?.let { window ->
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
        }
    }

    Crossfade(targetState = isDarkTheme, animationSpec = tween(300), label = "theme") { dark ->
        MaterialTheme(
            colorScheme = if (dark) StallFitDarkColorScheme else StallFitLightColorScheme,
            typography = StallFitTypography,
            content = content
        )
    }
}
