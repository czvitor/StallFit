package com.vitorsousa.stallfit.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/** The Dashboard's centerpiece: calories consumed today as a ring against the daily goal. */
@Composable
fun CalorieRing(
    consumed: Double,
    goal: Int,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 176.dp,
    strokeWidth: androidx.compose.ui.unit.Dp = 14.dp
) {
    val progress = if (goal <= 0) 0f else (consumed / goal).toFloat().coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 600),
        label = "calorieRing"
    )
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val ringColor = MaterialTheme.colorScheme.primary

    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke
            )
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = stroke
            )
        }
        RingCenterLabel(consumed = consumed, goal = goal)
    }
}

@Composable
private fun RingCenterLabel(consumed: Double, goal: Int) {
    Box(contentAlignment = Alignment.Center) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = consumed.roundToInt().toString(),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "de $goal kcal",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
