package com.bgcoding.notes.app.feature_note.domain.use_case

// this class will be a wrapper that will hold all use cases for a single feature
data class NoteUseCases(
    val getNotes: GetNotes,
    val deleteNote: DeleteNote,
    val addNote: AddNote,
    val getNote: GetNote
)