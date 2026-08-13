package com.vitorsousa.stallfit.data.repository

import com.vitorsousa.stallfit.core.util.normalizedKey
import com.vitorsousa.stallfit.data.backup.BackupEnvelope
import com.vitorsousa.stallfit.data.backup.BackupModule
import com.vitorsousa.stallfit.data.backup.BackupPayload
import com.vitorsousa.stallfit.data.backup.ImportStrategy
import com.vitorsousa.stallfit.data.backup.MealsBackupData
import com.vitorsousa.stallfit.data.backup.ProfileBackupData
import com.vitorsousa.stallfit.data.backup.WorkoutsBackupData
import com.vitorsousa.stallfit.data.local.StallFitDatabase
import com.vitorsousa.stallfit.data.local.dao.BackupDao
import com.vitorsousa.stallfit.data.local.entity.MacroGoalEntity
import com.vitorsousa.stallfit.data.local.entity.UserProfileEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** Modular JSON export/import por [BackupModule] — uma camada extra de segurança além das migrations do Room. */
class BackupRepository(private val backupDao: BackupDao) {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    suspend fun exportData(modules: Set<BackupModule>): String {
        require(modules.isNotEmpty()) { "Selecione ao menos uma categoria para exportar." }
        val orderedModules = BackupModule.entries.filter { it in modules }
        val envelope = BackupEnvelope(
            appSchemaVersion = StallFitDatabase.SCHEMA_VERSION,
            createdAt = System.currentTimeMillis(),
            modules = orderedModules,
            data = BackupPayload(
                workouts = if (BackupModule.WORKOUTS in modules) collectWorkoutsData() else null,
                meals = if (BackupModule.MEALS in modules) collectMealsData() else null,
                profile = if (BackupModule.PROFILE in modules) collectProfileData() else null
            )
        )
        return json.encodeToString(envelope)
    }

    /**
     * Faz o parse de [jsonString] e valida a versão de schema, sem escrever nada no banco — usado
     * para mostrar ao usuário o que um arquivo de backup contém antes de escolher o que restaurar.
     * Lança [kotlinx.serialization.SerializationException] para um arquivo malformado/corrompido e
     * [IllegalArgumentException] para divergência de schema ou backup vazio.
     */
    fun inspectBackup(jsonString: String): BackupEnvelope {
        val envelope = json.decodeFromString(BackupEnvelope.serializer(), jsonString)
        require(envelope.modules.isNotEmpty()) { "O arquivo não contém nenhuma categoria de dados reconhecida." }
        require(envelope.appSchemaVersion == StallFitDatabase.SCHEMA_VERSION) {
            "Backup incompatível: foi exportado do schema v${envelope.appSchemaVersion}, " +
                "mas o app atual usa o schema v${StallFitDatabase.SCHEMA_VERSION}."
        }
        return envelope
    }

    /** Aplica [selectedModules] de [envelope] usando [strategy]; módulos não selecionados, ou ausentes no arquivo, ficam intocados. */
    suspend fun importData(envelope: BackupEnvelope, selectedModules: Set<BackupModule>, strategy: ImportStrategy) {
        if (BackupModule.WORKOUTS in selectedModules) {
            envelope.data.workouts?.let { data ->
                if (strategy == ImportStrategy.OVERWRITE) backupDao.overwriteWorkouts(data) else mergeWorkouts(data)
            }
        }
        if (BackupModule.MEALS in selectedModules) {
            envelope.data.meals?.let { data ->
                if (strategy == ImportStrategy.OVERWRITE) backupDao.overwriteMeals(data) else mergeMeals(data)
            }
        }
        if (BackupModule.PROFILE in selectedModules) {
            envelope.data.profile?.let { data ->
                if (strategy == ImportStrategy.OVERWRITE) backupDao.overwriteProfile(data) else mergeProfile(data)
            }
        }
    }

    private suspend fun collectWorkoutsData() = WorkoutsBackupData(
        exercises = backupDao.getAllExercises(),
        workoutTemplates = backupDao.getAllWorkoutTemplates(),
        templateExercises = backupDao.getAllTemplateExercises(),
        workoutSessions = backupDao.getAllWorkoutSessions(),
        setEntries = backupDao.getAllSetEntries()
    )

    private suspend fun collectMealsData() = MealsBackupData(
        foods = backupDao.getAllFoods(),
        meals = backupDao.getAllMeals(),
        mealFoodItems = backupDao.getAllMealFoodItems()
    )

    private suspend fun collectProfileData() = ProfileBackupData(
        userProfiles = backupDao.getAllUserProfiles(),
        macroGoals = backupDao.getAllMacroGoals(),
        bodyMeasurements = backupDao.getAllBodyMeasurements()
    )

    // Exercícios/fichas/sessões/séries nunca carregam uma foreign key para os módulos de refeições
    // ou perfil (confirmado nas entities), então cada módulo pode ser mesclado independentemente,
    // sem remapeamento de id entre módulos.
    private suspend fun mergeWorkouts(data: WorkoutsBackupData) {
        val existingExercises = backupDao.getAllExercises()
        val exerciseIdMap = mutableMapOf<Long, Long>()
        for (exercise in data.exercises) {
            val existing = existingExercises.firstOrNull { it.name.normalizedKey() == exercise.name.normalizedKey() }
            exerciseIdMap[exercise.id] = existing?.id ?: backupDao.insertExercise(exercise.copy(id = 0))
        }

        val templateIdMap = mutableMapOf<Long, Long>()
        for (template in data.workoutTemplates) {
            templateIdMap[template.id] = backupDao.insertWorkoutTemplate(template.copy(id = 0))
        }

        val remappedTemplateExercises = data.templateExercises.mapNotNull { templateExercise ->
            val templateId = templateIdMap[templateExercise.templateId] ?: return@mapNotNull null
            val exerciseId = exerciseIdMap[templateExercise.exerciseId] ?: return@mapNotNull null
            templateExercise.copy(id = 0, templateId = templateId, exerciseId = exerciseId)
        }
        backupDao.insertTemplateExercises(remappedTemplateExercises)

        val sessionIdMap = mutableMapOf<Long, Long>()
        for (session in data.workoutSessions) {
            val remappedTemplateId = session.templateId?.let { templateIdMap[it] }
            sessionIdMap[session.id] = backupDao.insertWorkoutSession(session.copy(id = 0, templateId = remappedTemplateId))
        }

        val remappedSetEntries = data.setEntries.mapNotNull { setEntry ->
            val sessionId = sessionIdMap[setEntry.sessionId] ?: return@mapNotNull null
            val exerciseId = exerciseIdMap[setEntry.exerciseId] ?: return@mapNotNull null
            setEntry.copy(id = 0, sessionId = sessionId, exerciseId = exerciseId)
        }
        backupDao.insertSetEntries(remappedSetEntries)
    }

    private suspend fun mergeMeals(data: MealsBackupData) {
        val existingFoods = backupDao.getAllFoods()
        val foodIdMap = mutableMapOf<Long, Long>()
        for (food in data.foods) {
            val existing = existingFoods.firstOrNull { it.name.normalizedKey() == food.name.normalizedKey() }
            foodIdMap[food.id] = existing?.id ?: backupDao.insertFood(food.copy(id = 0))
        }

        val mealIdMap = mutableMapOf<Long, Long>()
        for (meal in data.meals) {
            mealIdMap[meal.id] = backupDao.insertMeal(meal.copy(id = 0))
        }

        val remappedItems = data.mealFoodItems.mapNotNull { item ->
            val mealId = mealIdMap[item.mealId] ?: return@mapNotNull null
            val foodId = foodIdMap[item.foodId] ?: return@mapNotNull null
            item.copy(id = 0, mealId = mealId, foodId = foodId)
        }
        backupDao.insertMealFoodItems(remappedItems)
    }

    // user_profile/macro_goal são tabelas singleton (id fixo, ver UserProfileEntity/MacroGoalEntity),
    // então "mesclar" não pode significar "adicionar uma segunda linha" — significa "só adotar o
    // valor do backup se este aparelho ainda não tiver um", em vez de sobrescrever silenciosamente
    // o que o usuário já tem agora.
    private suspend fun mergeProfile(data: ProfileBackupData) {
        if (backupDao.getUserProfileOnce() == null) {
            data.userProfiles.firstOrNull()?.let {
                backupDao.upsertUserProfile(it.copy(id = UserProfileEntity.SINGLETON_ID))
            }
        }
        if (backupDao.getMacroGoalOnce() == null) {
            data.macroGoals.firstOrNull()?.let {
                backupDao.upsertMacroGoal(it.copy(id = MacroGoalEntity.SINGLETON_ID))
            }
        }
        data.bodyMeasurements.forEach { measurement ->
            backupDao.insertBodyMeasurement(measurement.copy(id = 0))
        }
    }
}
