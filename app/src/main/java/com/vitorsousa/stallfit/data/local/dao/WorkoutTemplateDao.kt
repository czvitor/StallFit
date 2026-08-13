package com.vitorsousa.stallfit.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.vitorsousa.stallfit.data.local.entity.TemplateExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.WorkoutTemplateEntity
import com.vitorsousa.stallfit.data.local.relation.TemplateExerciseWithExercise
import com.vitorsousa.stallfit.data.local.relation.TemplateWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutTemplateDao {
    @Insert
    suspend fun insertTemplate(template: WorkoutTemplateEntity): Long

    @Insert
    suspend fun insertTemplateExercises(exercises: List<TemplateExerciseEntity>)

    @Transaction
    suspend fun insertTemplateWithExercises(
        template: WorkoutTemplateEntity,
        exercises: List<TemplateExerciseEntity>
    ): Long {
        val templateId = insertTemplate(template)
        insertTemplateExercises(exercises.map { it.copy(templateId = templateId) })
        return templateId
    }

    @Query("DELETE FROM workout_templates WHERE id = :templateId")
    suspend fun deleteTemplate(templateId: Long)

    @Query("UPDATE template_exercises SET referenceWeightKg = :weightKg WHERE id = :templateExerciseId")
    suspend fun updateReferenceWeight(templateExerciseId: Long, weightKg: Double?)

    @Query("UPDATE workout_templates SET notes = :notes WHERE id = :templateId")
    suspend fun updateNotes(templateId: Long, notes: String?)

    @Query("SELECT * FROM workout_templates WHERE id = :templateId")
    suspend fun getTemplateOnce(templateId: Long): WorkoutTemplateEntity?

    @Query("SELECT * FROM workout_templates WHERE id = :templateId")
    fun getTemplateFlow(templateId: Long): Flow<WorkoutTemplateEntity?>

    @Transaction
    @Query("SELECT * FROM workout_templates ORDER BY createdAt DESC")
    fun getAllTemplatesWithExercises(): Flow<List<TemplateWithExercises>>

    @Query(
        """
        SELECT template_exercises.*, exercises.name AS exerciseName,
               exercises.muscleGroup AS muscleGroup, exercises.equipment AS equipment
        FROM template_exercises
        JOIN exercises ON exercises.id = template_exercises.exerciseId
        WHERE template_exercises.templateId = :templateId
        ORDER BY template_exercises.orderIndex ASC
        """
    )
    fun getExercisesForTemplate(templateId: Long): Flow<List<TemplateExerciseWithExercise>>
}
