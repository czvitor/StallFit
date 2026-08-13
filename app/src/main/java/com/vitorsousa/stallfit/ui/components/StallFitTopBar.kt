package com.vitorsousa.stallfit.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vitorsousa.stallfit.R

/**
 * Fixed page header shown above the scrollable content of every top-level tab: the StällFit
 * symbol + name-only wordmark on top, then the current page's title below. Lives in [Scaffold]'s
 * `topBar` slot so it never scrolls with the page and stays in the same place across tab switches.
 */
@Composable
fun StallFitTopBar(
    title: String,
    isDarkTheme: Boolean = true,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(
                        if (isDarkTheme) R.drawable.stallfit_symbol else R.drawable.stallfit_symbol_light
                    ),
                    contentDescription = null,
                    modifier = Modifier.height(22.dp)
                )
                Image(
                    painter = painterResource(
                        if (isDarkTheme) R.drawable.stallfit_wordmark else R.drawable.stallfit_wordmark_light
                    ),
                    contentDescription = "StällFit",
                    modifier = Modifier.height(20.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )
                actions()
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}
