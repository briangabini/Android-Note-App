package com.bgcoding.notes.app.feature_note.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.platform.ClipboardManager
import com.bgcoding.notes.app.feature_note.domain.model.Note
import com.bgcoding.notes.app.feature_note.presentation.notes.NotesEvent

// for every single ui action, we create an event
sealed class AddEditNoteEvent {
    data class EnteredTitle(val value: String): AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditNoteEvent()
    data class EnteredContent(val value: String): AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddEditNoteEvent()
    data object SaveNote: AddEditNoteEvent()
    data class CopyContent(val clipboardManager: ClipboardManager): AddEditNoteEvent()
    data object DeleteNote: AddEditNoteEvent()
}