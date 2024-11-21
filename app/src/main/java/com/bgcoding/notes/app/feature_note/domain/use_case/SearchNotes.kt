package com.bgcoding.notes.app.feature_note.domain.use_case

import com.bgcoding.notes.app.feature_note.domain.repository.NoteRepository
import com.bgcoding.notes.app.feature_note.domain.util.NoteOrder

class SearchNotes(private val repository: NoteRepository) {
    operator fun invoke(query: String) =
        repository.searchNotes(query)
}