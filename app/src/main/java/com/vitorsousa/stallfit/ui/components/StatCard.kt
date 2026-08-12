package com.vitorsousa.stallfit.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/** A compact metric tile used across the Dashboard — a label, a big value, and a muted caption. */
@Composable
fun StatCard(
    label: String,
    value: String,
    caption: String,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            val baseValueFontSize = MaterialTheme.typography.headlineMedium.fontSize
            var valueFontSize by remember(value) { mutableStateOf(baseValueFontSize) }
            var valueReadyToDraw by remember(value) { mutableStateOf(false) }
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = valueFontSize),
                color = accentColor,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip,
                modifier = Modifier.drawWithContent { if (valueReadyToDraw) drawContent() },
                onTextLayout = { result ->
                    if (result.didOverflowWidth) {
                        valueFontSize *= 0.9f
                    } else {
                        valueReadyToDraw = true
                    }
                }
            )
            Text(
                text = caption,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
