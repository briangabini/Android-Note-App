package com.bgcoding.notes.app.ui.theme

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    val dataStore: DataStore<Preferences>,
) : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme


    init {
        viewModelScope.launch {
            dataStore.data.map { preferences ->
                preferences[booleanPreferencesKey("nightMode")] ?: false
            }.collect { isDark ->
                _isDarkTheme.value = isDark
            }
        }
    }

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[booleanPreferencesKey("nightMode")] = isDark
            }
            _isDarkTheme.value = isDark
        }
    }
}