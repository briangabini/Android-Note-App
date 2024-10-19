package com.bgcoding.notes.app.feature_note.presentation.util

// This class contains the screens and the corresponding routes
sealed class Screen(val route: String) {
    object NotesScreen: Screen("notes_screen")
    object AddEditNoteScreen: Screen(" add_edit_note_screen")
}