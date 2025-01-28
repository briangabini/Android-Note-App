package com.bgcoding.notes.app.feature_note.presentation.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bgcoding.notes.app.feature_note.presentation.notes.NotesEvent
import com.bgcoding.notes.app.feature_note.presentation.notes.NotesEvent.DeleteAllNotesPermanently
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val dataStore: DataStore<Preferences>,
) : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme


    private val _isShowDateEnabled = MutableStateFlow(false)
    val isShowDateEnabled: StateFlow<Boolean> = _isShowDateEnabled

    init {
        viewModelScope.launch {
            dataStore.data.map { preferences ->
                preferences[booleanPreferencesKey("nightMode")] ?: false
            }.collect { isDark ->
                _isDarkTheme.value = isDark
            }
        }
        viewModelScope.launch {
            dataStore.data.map { preferences ->
                preferences[booleanPreferencesKey("showDate")] ?: false
            }.collect { isShowDate ->
                _isShowDateEnabled.value = isShowDate
            }
        }
    }

    fun onEvent(event: SettingsEvent) {
        when(event) {
            is SettingsEvent.ToggleNightMode -> toggleTheme(event.nightModeOn);
            is SettingsEvent.SetShowNoteModifyDates -> setShowDate(event.showDates);
        }
    }

    private fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[booleanPreferencesKey("nightMode")] = isDark
            }
            _isDarkTheme.value = isDark
        }
    }

    private fun setShowDate(isShowDate: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[booleanPreferencesKey("showDate")] = isShowDate
            }
            _isShowDateEnabled.value = isShowDate
        }
    }
}