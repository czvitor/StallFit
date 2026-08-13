package com.vitorsousa.stallfit.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vitorsousa.stallfit.data.backup.BackupEnvelope
import com.vitorsousa.stallfit.data.backup.BackupModule
import com.vitorsousa.stallfit.data.backup.ImportStrategy
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private fun formatBackupDate(epochMillis: Long): String =
    Instant.ofEpochMilli(epochMillis)
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))

@Composable
fun ExportBackupDialog(
    onDismiss: () -> Unit,
    onConfirm: (Set<BackupModule>) -> Unit
) {
    var selected by remember { mutableStateOf(setOf<BackupModule>()) }
    val allSelected = selected.size == BackupModule.entries.size

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Exportar dados") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Escolha quais categorias vão para o arquivo de backup.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                BackupCheckboxRow(
                    label = "Selecionar tudo (backup completo)",
                    description = null,
                    checked = allSelected,
                    onCheckedChange = { checked -> selected = if (checked) BackupModule.entries.toSet() else emptySet() }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                BackupModule.entries.forEach { module ->
                    BackupCheckboxRow(
                        label = module.displayName,
                        description = module.description,
                        checked = module in selected,
                        onCheckedChange = { checked -> selected = if (checked) selected + module else selected - module }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected) }, enabled = selected.isNotEmpty()) {
                Text("Exportar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun ImportBackupDialog(
    envelope: BackupEnvelope,
    onDismiss: () -> Unit,
    onConfirm: (Set<BackupModule>, ImportStrategy) -> Unit
) {
    var selected by remember { mutableStateOf(envelope.modules.toSet()) }
    var strategy by remember { mutableStateOf(ImportStrategy.MERGE) }
    val allSelected = selected.size == envelope.modules.size

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Restaurar backup") },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 420.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Arquivo gerado em ${formatBackupDate(envelope.createdAt)}. Escolha o que restaurar:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (envelope.modules.size > 1) {
                    BackupCheckboxRow(
                        label = "Selecionar tudo",
                        description = null,
                        checked = allSelected,
                        onCheckedChange = { checked -> selected = if (checked) envelope.modules.toSet() else emptySet() }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                }
                envelope.modules.forEach { module ->
                    BackupCheckboxRow(
                        label = module.displayName,
                        description = module.description,
                        checked = module in selected,
                        onCheckedChange = { checked -> selected = if (checked) selected + module else selected - module }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Como aplicar",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                BackupStrategyRow(
                    label = "Mesclar / Somar",
                    description = "Mantém os dados atuais e adiciona os do arquivo.",
                    selected = strategy == ImportStrategy.MERGE,
                    onClick = { strategy = ImportStrategy.MERGE }
                )
                BackupStrategyRow(
                    label = "Substituir",
                    description = "Apaga os dados atuais das categorias marcadas acima e usa só os do arquivo.",
                    selected = strategy == ImportStrategy.OVERWRITE,
                    onClick = { strategy = ImportStrategy.OVERWRITE }
                )
                if (strategy == ImportStrategy.OVERWRITE) {
                    Text(
                        text = "Atenção: os dados atuais das categorias selecionadas serão apagados. Essa ação não pode ser desfeita.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(selected, strategy) },
                enabled = selected.isNotEmpty()
            ) {
                Text(
                    text = if (strategy == ImportStrategy.OVERWRITE) "Substituir" else "Restaurar",
                    color = if (strategy == ImportStrategy.OVERWRITE) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun BackupCheckboxRow(
    label: String,
    description: String?,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Column(modifier = Modifier.padding(start = 4.dp)) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            if (description != null) {
                Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun BackupStrategyRow(
    label: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onClick)
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Column(modifier = Modifier.padding(start = 4.dp)) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
