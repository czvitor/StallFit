package com.vitorsousa.stallfit.ui.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.data.repository.ProfileRepository
import com.vitorsousa.stallfit.data.repository.WorkoutTemplateRepository
import com.vitorsousa.stallfit.navigation.Destination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Owns the static ficha (reference weights, notes, delete, PDF export) — never writes a
 * [com.vitorsousa.stallfit.data.local.entity.SetEntryEntity]. Tonnage-affecting logging only
 * happens in an Active Workout Session, started here via [startSession] and executed in
 * [ActiveWorkoutViewModel].
 */
class WorkoutTemplateDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val workoutTemplateRepository: WorkoutTemplateRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val templateId: Long =
        checkNotNull(savedStateHandle[Destination.WorkoutTemplateDetail.ARG_TEMPLATE_ID])

    val uiState: StateFlow<WorkoutTemplateDetailUiState> = combine(
        workoutTemplateRepository.getTemplateFlow(templateId),
        workoutTemplateRepository.getExercisesForTemplate(templateId),
        profileRepository.profile
    ) { template, exercises, profile ->
        WorkoutTemplateDetailUiState(
            template = template,
            exercises = exercises,
            studentName = profile?.name.orEmpty(),
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), WorkoutTemplateDetailUiState())

    /** Updates the reference/base weight shown for consultation — never feeds tonnage math. */
    fun updateReferenceWeight(templateExerciseId: Long, weightKg: Double?) {
        viewModelScope.launch {
            workoutTemplateRepository.updateReferenceWeight(templateExerciseId, weightKg)
        }
    }

    fun updateNotes(notes: String) {
        viewModelScope.launch {
            workoutTemplateRepository.updateNotes(templateId, notes)
        }
    }

    fun deleteTemplate(onDeleted: () -> Unit) {
        viewModelScope.launch {
            workoutTemplateRepository.deleteTemplate(templateId)
            onDeleted()
        }
    }

    /** Starts (or resumes today's) Active Workout Session for this ficha — the only place set entries get logged. */
    fun startSession(onStarted: (Long) -> Unit) {
        viewModelScope.launch {
            val sessionId = workoutTemplateRepository.getOrStartSessionForTemplate(templateId)
            onStarted(sessionId)
        }
    }
}
