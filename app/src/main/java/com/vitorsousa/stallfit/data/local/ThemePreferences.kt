package com.vitorsousa.stallfit.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.themeDataStore by preferencesDataStore(name = "theme_preferences")

/**
 * Persists the user's manual dark/light choice. A `null` value means no choice has been made
 * yet, in which case StällFit defaults to its dark theme rather than following the OS setting.
 */
class ThemePreferences(private val context: Context) {
    private val isDarkThemeKey = booleanPreferencesKey("is_dark_theme")

    val isDarkTheme: Flow<Boolean?> = context.themeDataStore.data.map { preferences ->
        preferences[isDarkThemeKey]
    }

    suspend fun setDarkTheme(isDark: Boolean) {
        context.themeDataStore.edit { preferences ->
            preferences[isDarkThemeKey] = isDark
        }
    }
}
