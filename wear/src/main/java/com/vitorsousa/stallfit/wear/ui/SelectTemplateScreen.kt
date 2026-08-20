package com.vitorsousa.stallfit.wear.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.vitorsousa.stallfit.wear.data.WearTemplateDto

@Composable
fun SelectTemplateScreen(
    catalogState: CatalogState,
    onSelectTemplate: (Long?) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenScaffold(modifier = modifier) {
        when (catalogState) {
            is CatalogState.Loading -> ScalingLazyColumn(modifier = Modifier.fillMaxSize()) {
                item { Text(text = "Carregando...") }
            }

            is CatalogState.Error -> ScalingLazyColumn(modifier = Modifier.fillMaxSize()) {
                item { Text(text = catalogState.message) }
                item { Button(onClick = onRetry) { Text("Tentar de novo") } }
            }

            is CatalogState.Loaded -> ScalingLazyColumn(modifier = Modifier.fillMaxSize()) {
                item { Text(text = "Selecionar ficha", style = MaterialTheme.typography.titleMedium) }
                items(catalogState.catalog.templates) { template: WearTemplateDto ->
                    Button(onClick = { onSelectTemplate(template.id) }) { Text(template.title) }
                }
                item { Button(onClick = { onSelectTemplate(null) }) { Text("Treino livre") } }
            }
        }
    }
}
