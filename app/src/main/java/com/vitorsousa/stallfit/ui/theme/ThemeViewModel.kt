package com.vitorsousa.stallfit.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorsousa.stallfit.data.repository.ThemeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Drives the app's dark/light choice. A `null` saved preference defaults to dark — StällFit is
 * dark-first by design, so unlike most theme togglers this does not follow the OS setting on
 * first launch.
 */
class ThemeViewModel(private val themeRepository: ThemeRepository) : ViewModel() {
    val isDarkTheme: StateFlow<Boolean> = themeRepository.isDarkTheme
        .map { it ?: true }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)

    fun toggleTheme() {
        viewModelScope.launch {
            themeRepository.setDarkTheme(!isDarkTheme.value)
        }
    }
}
