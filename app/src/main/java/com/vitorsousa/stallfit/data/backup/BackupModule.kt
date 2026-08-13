package com.vitorsousa.stallfit.data.backup

import java.time.LocalDate
import kotlinx.serialization.Serializable

/**
 * One independently exportable/importable slice of the database, selectable by the user in the
 * Backup e Restauração section do Perfil. As três nunca compartilham uma foreign key entre si,
 * então cada uma pode ser exportada, mesclada ou substituída isoladamente.
 */
@Serializable
enum class BackupModule(val displayName: String, val description: String, val fileTag: String) {
    WORKOUTS(
        displayName = "Treinos e Registros de Carga",
        description = "Histórico de treinos criados, séries, repetições e evolução de cargas em kg",
        fileTag = "treinos"
    ),
    MEALS(
        displayName = "Refeições e Alimentos",
        description = "Refeições personalizadas salvas no cardápio de café, almoço, lanche, jantar etc.",
        fileTag = "refeicoes"
    ),
    PROFILE(
        displayName = "Perfil e Evolução Física",
        description = "Dados do perfil, histórico de peso, altura, circunferências e bioimpedância",
        fileTag = "perfil"
    )
}

/** Como as linhas de um módulo importado são aplicadas quando o banco local já tem dados ali. */
enum class ImportStrategy {
    /** Mantém todas as linhas locais e acrescenta as do arquivo (IDs são remapeados, sem colisão). */
    MERGE,

    /** Apaga todas as linhas locais do módulo antes de inserir as linhas do arquivo no lugar. */
    OVERWRITE
}

/** `backup_fit_completo_2026-08-13.json`, `backup_fit_treinos_2026-08-13.json`, `backup_fit_treinos_refeicoes_2026-08-13.json`. */
fun backupFileName(modules: Set<BackupModule>, today: LocalDate = LocalDate.now()): String {
    val tag = if (modules.containsAll(BackupModule.entries)) {
        "completo"
    } else {
        BackupModule.entries.filter { it in modules }.joinToString("_") { it.fileTag }
    }
    return "backup_fit_${tag}_$today.json"
}
