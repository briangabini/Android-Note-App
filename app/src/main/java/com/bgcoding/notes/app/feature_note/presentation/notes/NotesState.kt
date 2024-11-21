package com.bgcoding.notes.app.feature_note.presentation.notes

import com.bgcoding.notes.app.feature_note.domain.model.Note
import com.bgcoding.notes.app.feature_note.domain.util.NoteOrder
import com.bgcoding.notes.app.feature_note.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
    val showDeleted: Boolean = false,
    val searchNotes: String = ""
)