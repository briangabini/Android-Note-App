package com.bgcoding.notes.app.feature_note.domain.use_case

import com.bgcoding.notes.app.feature_note.domain.model.InvalidNoteException
import com.bgcoding.notes.app.feature_note.domain.model.Note
import com.bgcoding.notes.app.feature_note.domain.repository.NoteRepository

class AddNote(
private val repository: NoteRepository
) {

    @Throws(IllegalArgumentException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            note.title = "Untitled"
        }

        repository.insertNote(note)
    }
}