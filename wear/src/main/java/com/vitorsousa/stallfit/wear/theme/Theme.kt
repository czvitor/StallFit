package com.vitorsousa.stallfit.wear.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material3.ColorScheme
import androidx.wear.compose.material3.MaterialTheme

// Duplicated from the phone's ui/theme/Color.kt — the watch app is dark-only and needs only the
// handful of colors used by its two screens (see WearDto/WearMessagePaths for the same deliberate
// duplication rationale — not worth a shared :common module for this).
val VoltNeon = Color(0xFFCCFF00)
val VoltNeonVariant = Color(0xFFA6D400)
val ObsidianBackground = Color(0xFF0D0F12)
val SurfaceContainerLow = Color(0xFF12151A)
val DarkSlateCard = Color(0xFF16191E)
val SurfaceContainerHighest = Color(0xFF2A2F3A)
val SteelBorder = Color(0xFF2E333D)
val OutlineVariant = Color(0xFF23262E)
val TextPrimary = Color(0xFFF0F3F8)
val TextSecondary = Color(0xFF9EA7B6)
val StatusWarning = Color(0xFFFF3B30)
val ErrorContainerTint = Color(0xFF3A1613)

private val StallFitWearColorScheme = ColorScheme(
    primary = VoltNeon,
    primaryDim = VoltNeonVariant,
    onPrimary = ObsidianBackground,
    background = ObsidianBackground,
    onBackground = TextPrimary,
    surfaceContainer = DarkSlateCard,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainerHigh = SurfaceContainerHighest,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = SteelBorder,
    outlineVariant = OutlineVariant,
    error = StatusWarning,
    errorContainer = ErrorContainerTint,
    onError = ObsidianBackground,
    onErrorContainer = TextPrimary
)

@Composable
fun StallFitWearTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = StallFitWearColorScheme, content = content)
}
