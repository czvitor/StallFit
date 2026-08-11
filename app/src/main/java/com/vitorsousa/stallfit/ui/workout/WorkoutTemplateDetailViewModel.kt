package com.vitorsousa.stallfit.ui.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.data.local.relation.TemplateExerciseWithExercise
import com.vitorsousa.stallfit.data.repository.WorkoutRepository
import com.vitorsousa.stallfit.data.repository.WorkoutTemplateRepository
import com.vitorsousa.stallfit.navigation.Destination
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class WorkoutTemplateDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val workoutTemplateRepository: WorkoutTemplateRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val templateId: Long =
        checkNotNull(savedStateHandle[Destination.WorkoutTemplateDetail.ARG_TEMPLATE_ID])

    private val _sessionId = MutableStateFlow<Long?>(null)
    val sessionId: StateFlow<Long?> = _sessionId.asStateFlow()

    init {
        viewModelScope.launch {
            _sessionId.value = workoutTemplateRepository.getOrStartSessionForTemplate(templateId)
        }
    }

    val uiState: StateFlow<WorkoutTemplateDetailUiState> = combine(
        workoutTemplateRepository.getTemplateFlow(templateId),
        workoutTemplateRepository.getExercisesForTemplate(templateId),
        _sessionId.flatMapLatest { id -> id?.let(workoutRepository::getSessionFlow) ?: flowOf(null) }
    ) { template, exercises, session ->
        WorkoutTemplateDetailUiState(
            template = template,
            exercises = exercises,
            session = session,
            isLoading = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), WorkoutTemplateDetailUiState())

    fun saveRegistro(exercise: TemplateExerciseWithExercise, weightKg: Double) {
        val sessionId = _sessionId.value ?: return
        viewModelScope.launch {
            repeat(exercise.templateExercise.sets) { index ->
                workoutRepository.logSet(
                    sessionId = sessionId,
                    exerciseId = exercise.templateExercise.exerciseId,
                    setNumber = index + 1,
                    reps = exercise.templateExercise.repRangeMax,
                    weightKg = weightKg
                )
            }
        }
    }

    fun finishWorkout(onDone: () -> Unit) {
        val session = uiState.value.session ?: return
        viewModelScope.launch {
            workoutRepository.finishSession(session)
            onDone()
        }
    }
}
