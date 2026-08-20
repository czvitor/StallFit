package com.vitorsousa.stallfit.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.core.util.DateUtils
import com.vitorsousa.stallfit.data.repository.WorkoutRepository
import com.vitorsousa.stallfit.data.repository.WorkoutTemplateRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WorkoutHomeViewModel(
    private val workoutRepository: WorkoutRepository,
    private val workoutTemplateRepository: WorkoutTemplateRepository
) : ViewModel() {

    val uiState: StateFlow<WorkoutHomeUiState> = combine(
        workoutRepository.activeSession,
        workoutRepository.completedSessions,
        workoutRepository.getVolumeThisWeek(),
        workoutTemplateRepository.allTemplatesWithExercises
    ) { active, completed, volume, templates ->
        WorkoutHomeUiState(active, completed, volume, templates)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = WorkoutHomeUiState()
    )

    fun startNewSession(onCreated: (Long) -> Unit) {
        viewModelScope.launch {
            val sessionId = workoutRepository.startSession(DateUtils.todaySessionLabel())
            onCreated(sessionId)
        }
    }
}
