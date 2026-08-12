package com.vitorsousa.stallfit.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitorsousa.stallfit.R
import com.vitorsousa.stallfit.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val symbolScale = remember { Animatable(0.5f) }
    val symbolAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch { symbolScale.animateTo(1f, animationSpec = tween(550, easing = FastOutSlowInEasing)) }
        launch { symbolAlpha.animateTo(1f, animationSpec = tween(350, easing = LinearOutSlowInEasing)) }
        delay(550)
        textAlpha.animateTo(1f, animationSpec = tween(250, easing = LinearOutSlowInEasing))
        delay(650)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.stallfit_symbol),
                contentDescription = null,
                modifier = Modifier
                    .width(140.dp)
                    .aspectRatio(661.94f / 423.44f)
                    .graphicsLayer(
                        scaleX = symbolScale.value,
                        scaleY = symbolScale.value,
                        alpha = symbolAlpha.value
                    )
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.graphicsLayer(alpha = textAlpha.value)
            ) {
                Image(
                    painter = painterResource(R.drawable.stallfit_wordmark),
                    contentDescription = "StällFit",
                    modifier = Modifier
                        .width(220.dp)
                        .aspectRatio(1103.94f / 195.94f)
                )
                Text(
                    text = "TREINO • NUTRIÇÃO",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
