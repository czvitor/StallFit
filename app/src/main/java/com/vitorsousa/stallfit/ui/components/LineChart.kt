package com.vitorsousa.stallfit.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

/** One data point in a [LineChart]: a value plotted on the Y axis and a short label for the X axis. */
data class LineChartPoint(val value: Float, val label: String)

/**
 * Minimal hand-rolled line chart (no charting library dependency) for a metric evolving across a
 * handful of points — body weight per assessment, best set weight per workout session, etc.
 * Points must already be in the order they should be drawn left-to-right (oldest first).
 */
@Composable
fun LineChart(
    points: List<LineChartPoint>,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    valueFormatter: (Float) -> String = { value ->
        if (value == value.toLong().toFloat()) value.toLong().toString() else "%.1f".format(value)
    }
) {
    if (points.size < 2) {
        Text(
            text = "Registre pelo menos 2 valores para ver a evolução em gráfico.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
        return
    }

    val maxValue = points.maxOf { it.value }
    val minValue = points.minOf { it.value }
    val range = maxValue - minValue
    val gridColor = MaterialTheme.colorScheme.outlineVariant
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant
    val labelStyle = MaterialTheme.typography.labelSmall

    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = valueFormatter(maxValue), style = labelStyle, color = textColor)
            Text(text = valueFormatter(minValue), style = labelStyle, color = textColor)
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(vertical = 8.dp)
        ) {
            val stepX = size.width / (points.size - 1)
            fun yFor(value: Float): Float =
                if (range == 0f) size.height / 2f else size.height - ((value - minValue) / range) * size.height

            drawLine(
                color = gridColor,
                start = Offset(0f, size.height / 2f),
                end = Offset(size.width, size.height / 2f),
                strokeWidth = 1.dp.toPx()
            )

            val offsets = points.mapIndexed { index, point -> Offset(index * stepX, yFor(point.value)) }

            for (i in 0 until offsets.size - 1) {
                drawLine(
                    color = lineColor,
                    start = offsets[i],
                    end = offsets[i + 1],
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
            offsets.forEach { offset -> drawCircle(color = lineColor, radius = 4.dp.toPx(), center = offset) }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = points.first().label, style = labelStyle, color = textColor)
            Text(text = points.last().label, style = labelStyle, color = textColor)
        }
    }
}
