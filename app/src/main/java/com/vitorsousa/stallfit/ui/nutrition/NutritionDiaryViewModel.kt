package com.vitorsousa.stallfit.ui.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.core.util.DateUtils
import com.vitorsousa.stallfit.data.local.entity.MealEntryEntity
import com.vitorsousa.stallfit.data.repository.NutritionRepository
import com.vitorsousa.stallfit.domain.model.toMacroTotals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NutritionDiaryViewModel(
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(DateUtils.todayEpochDay())
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    val uiState: StateFlow<NutritionDiaryUiState> = combine(
        _selectedDate,
        _selectedDate.flatMapLatest { date -> nutritionRepository.getEntriesForDate(date) },
        nutritionRepository.macroGoal
    ) { date, entries, goal ->
        NutritionDiaryUiState(
            dateEpochDay = date,
            entries = entries,
            macroGoal = goal,
            totals = entries.toMacroTotals()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = NutritionDiaryUiState()
    )

    fun previousDay() {
        _selectedDate.value -= 1
    }

    fun nextDay() {
        if (_selectedDate.value < DateUtils.todayEpochDay()) {
            _selectedDate.value += 1
        }
    }

    fun deleteEntry(entry: MealEntryEntity) {
        viewModelScope.launch { nutritionRepository.deleteMealEntry(entry) }
    }
}
