package com.bgcoding.notes.app.feature_note.domain.repository

import com.bgcoding.notes.app.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotes(query: String? = null): Flow<List<Note>>

    suspend fun getNoteById(id: Int): Note?

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun deletedAllNotesPermanently()
}