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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vitorsousa.stallfit.R
import com.vitorsousa.stallfit.ui.theme.ObsidianBackground
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
        delay(650)
        textAlpha.animateTo(1f, animationSpec = tween(350, easing = LinearOutSlowInEasing))
        delay(1400)
        onFinished()
    }

    // Fixed to the dark brand color regardless of the active app theme: the logo assets below
    // are dark-only, and the splash is a static brand moment rather than themed content.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ObsidianBackground),
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

            Image(
                painter = painterResource(R.drawable.stallfit_nome_subtitulo),
                contentDescription = "StällFit — Treino • Nutrição",
                modifier = Modifier
                    .width(260.dp)
                    .aspectRatio(1104.46f / 279.96f)
                    .graphicsLayer(alpha = textAlpha.value)
            )
        }
    }
}
