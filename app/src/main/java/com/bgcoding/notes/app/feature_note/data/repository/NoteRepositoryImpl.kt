package com.bgcoding.notes.app.feature_note.data.repository

import com.bgcoding.notes.app.feature_note.data.data_source.NoteDao
import com.bgcoding.notes.app.feature_note.domain.model.Note
import com.bgcoding.notes.app.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {
    override fun getNotes(query: String?): Flow<List<Note>> {
        return if (query.isNullOrEmpty()) {
            dao.getNotes("")
        } else {
            dao.getNotes(query)
        }
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }

    override suspend fun deletedAllNotesPermanently() {
        dao.deletedAllNotesPermanently()
    }
}