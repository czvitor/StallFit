package com.vitorsousa.stallfit.data.repository

import com.vitorsousa.stallfit.data.local.ThemePreferences
import kotlinx.coroutines.flow.Flow

/** Wraps [ThemePreferences]; a `null` [isDarkTheme] means "use the app's dark-first default". */
class ThemeRepository(private val themePreferences: ThemePreferences) {
    val isDarkTheme: Flow<Boolean?> = themePreferences.isDarkTheme

    suspend fun setDarkTheme(isDark: Boolean) {
        themePreferences.setDarkTheme(isDark)
    }
}
