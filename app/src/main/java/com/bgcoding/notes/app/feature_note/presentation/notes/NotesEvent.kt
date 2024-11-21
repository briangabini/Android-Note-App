package com.bgcoding.notes.app.feature_note.presentation.notes

import com.bgcoding.notes.app.feature_note.domain.model.Note
import com.bgcoding.notes.app.feature_note.domain.util.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder): NotesEvent()
    data class DeleteNote(val note: Note): NotesEvent()
    data class SetShowDeleted(val showDeleted: Boolean) : NotesEvent()
    data object RestorePreviouslyDeletedNote: NotesEvent()
    data object ToggleOrderSection: NotesEvent()
    data object DeleteAllNotesPermanently: NotesEvent()
    data class RestoreDeletedNote(val note: Note): NotesEvent()
    data class SearchNotes(val query: String): NotesEvent()
}