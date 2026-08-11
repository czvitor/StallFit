package com.vitorsousa.stallfit.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class WorkoutHomeViewModel(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    val uiState: StateFlow<WorkoutHomeUiState> = combine(
        workoutRepository.activeSession,
        workoutRepository.completedSessions,
        workoutRepository.getVolumeThisWeek()
    ) { active, completed, volume ->
        WorkoutHomeUiState(active, completed, volume)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = WorkoutHomeUiState()
    )

    fun startNewSession(onCreated: (Long) -> Unit) {
        viewModelScope.launch {
            val label = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("EEEE, dd/MM", Locale("pt", "BR")))
                .replaceFirstChar { it.uppercase() }
            val sessionId = workoutRepository.startSession(label)
            onCreated(sessionId)
        }
    }
}
