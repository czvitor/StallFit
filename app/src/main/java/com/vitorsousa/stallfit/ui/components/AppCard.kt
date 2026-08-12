package com.vitorsousa.stallfit.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * The standard card treatment used across StällFit's screens: rounded corners, a subtle
 * border, and a hint of elevation, so containers read as distinct surfaces instead of flat
 * blocks of the same background color. Optionally clickable via [onClick].
 */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(20.dp),
    colors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border: BorderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    content: @Composable () -> Unit
) {
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            colors = colors,
            elevation = elevation,
            border = border,
            content = { content() }
        )
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            colors = colors,
            elevation = elevation,
            border = border,
            content = { content() }
        )
    }
}
