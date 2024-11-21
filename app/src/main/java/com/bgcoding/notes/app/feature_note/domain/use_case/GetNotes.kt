package com.bgcoding.notes.app.feature_note.domain.use_case

import android.util.Log
import com.bgcoding.notes.app.feature_note.domain.model.Note
import com.bgcoding.notes.app.feature_note.domain.repository.NoteRepository
import com.bgcoding.notes.app.feature_note.domain.util.NoteOrder
import com.bgcoding.notes.app.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class RetrieveMode {
    ShowNonDeleted,
    ShowDeleted,
    ShowAll
}

// a use case is a class that contains the business logic for a specific use case in the app
// it should contain one public function that will be called from the presentation layer, and possible private functions (for utility) that will be called from the public function
class GetNotes(
    private val repository: NoteRepository,
) {

    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
        mode: RetrieveMode = RetrieveMode.ShowNonDeleted
    ): Flow<List<Note>> {

        val notesFlow = repository
            .getNotes()
            .map { notes ->
                val filteredNotes = when(mode) {
                    RetrieveMode.ShowNonDeleted -> notes.filter { !it.deleted }
                    RetrieveMode.ShowDeleted -> notes.filter { it.deleted }
                    else -> notes
                }
                Log.d("GetNotes", "Filtered Notes: $filteredNotes")
                filteredNotes
            }
            .map { notes ->
                val sortedNotes = when(noteOrder.orderType) {
                    is OrderType.Ascending -> {
                        when(noteOrder) {
                            is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                            is NoteOrder.Date -> notes.sortedBy { it.timestamp }
                        }
                    }
                    is OrderType.Descending -> {
                        when(noteOrder) {
                            is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                            is NoteOrder.Date -> notes.sortedByDescending  { it.timestamp }
                        }
                    }
                }
                Log.d("GetNotes", "Sorted Notes: $sortedNotes")
                sortedNotes
            }

        return notesFlow
    }
}