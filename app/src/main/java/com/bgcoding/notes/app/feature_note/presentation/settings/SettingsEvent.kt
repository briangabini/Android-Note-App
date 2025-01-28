package com.bgcoding.notes.app.feature_note.presentation.settings

sealed class SettingsEvent {
    data class ToggleNightMode(val nightModeOn: Boolean): SettingsEvent()
    data class SetShowNoteModifyDates(val showDates: Boolean) : SettingsEvent()
}