package com.vitorsousa.stallfit.data.backup

import com.vitorsousa.stallfit.data.local.entity.BodyMeasurementEntity
import com.vitorsousa.stallfit.data.local.entity.ExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.FoodEntity
import com.vitorsousa.stallfit.data.local.entity.MacroGoalEntity
import com.vitorsousa.stallfit.data.local.entity.MealEntity
import com.vitorsousa.stallfit.data.local.entity.MealFoodItemEntity
import com.vitorsousa.stallfit.data.local.entity.SetEntryEntity
import com.vitorsousa.stallfit.data.local.entity.TemplateExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.UserProfileEntity
import com.vitorsousa.stallfit.data.local.entity.WorkoutSessionEntity
import com.vitorsousa.stallfit.data.local.entity.WorkoutTemplateEntity
import kotlinx.serialization.Serializable

/** [BackupModule.WORKOUTS] slice: catálogo de exercícios, fichas de treino e todas as séries registradas. */
@Serializable
data class WorkoutsBackupData(
    val exercises: List<ExerciseEntity>,
    val workoutTemplates: List<WorkoutTemplateEntity>,
    val templateExercises: List<TemplateExerciseEntity>,
    val workoutSessions: List<WorkoutSessionEntity>,
    val setEntries: List<SetEntryEntity>
)

/** [BackupModule.MEALS] slice: catálogo de alimentos e as refeições reutilizáveis montadas a partir dele. */
@Serializable
data class MealsBackupData(
    val foods: List<FoodEntity>,
    val meals: List<MealEntity>,
    val mealFoodItems: List<MealFoodItemEntity>
)

/** [BackupModule.PROFILE] slice: perfil e meta de macros (tabelas singleton) mais o histórico de medidas. */
@Serializable
data class ProfileBackupData(
    val userProfiles: List<UserProfileEntity>,
    val macroGoals: List<MacroGoalEntity>,
    val bodyMeasurements: List<BodyMeasurementEntity>
)

/** Guarda os slices de [BackupModule] selecionados no momento da exportação; os não selecionados ficam null. */
@Serializable
data class BackupPayload(
    val workouts: WorkoutsBackupData? = null,
    val meals: MealsBackupData? = null,
    val profile: ProfileBackupData? = null
)

/**
 * Envelope JSON modular de backup. [modules] lista exatamente quais slices [data] carrega — o app
 * lê isso na importação para mostrar ao usuário o que o arquivo contém antes de perguntar o que
 * restaurar. [appSchemaVersion] é checado contra o `@Database(version = ...)` atual do banco na
 * importação — uma divergência significa que o backup é de antes de uma migration e não pode ser
 * confiável para restaurar corretamente, então a importação precisa recusar em vez de adivinhar.
 */
@Serializable
data class BackupEnvelope(
    val formatVersion: String = FORMAT_VERSION,
    val appSchemaVersion: Int,
    val createdAt: Long,
    val modules: List<BackupModule>,
    val data: BackupPayload
) {
    companion object {
        const val FORMAT_VERSION = "1.0"
    }
}
