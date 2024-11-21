package com.bgcoding.notes.app.feature_note.domain.use_case

import com.bgcoding.notes.app.feature_note.domain.model.Note
import com.bgcoding.notes.app.feature_note.domain.repository.NoteRepository

class DeleteAllNotesPermanently(
    private val repository: NoteRepository
) {

    suspend operator fun invoke() {
        repository.deletedAllNotesPermanently()
    }
}