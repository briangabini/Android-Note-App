package com.bgcoding.notes.app.feature_note.domain.use_case

import com.bgcoding.notes.app.feature_note.domain.model.Note
import com.bgcoding.notes.app.feature_note.domain.repository.NoteRepository
import com.bgcoding.notes.app.feature_note.domain.util.NoteOrder
import com.bgcoding.notes.app.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// a use case is a class that contains the business logic for a specific use case in the app
// it should contain one public function that will be called from the presentation layer, and possible private functions (for utility) that will be called from the public function
class GetNotes(
    private val repository: NoteRepository
) {

    // we can use the invoke operator to make the class callable
    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Flow<List<Note>> {

        // instead of creating sql queries to do this, we can use the repository to get the notes and then sort them
        return repository.getNotes().map { notes ->
            when(noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when(noteOrder) {
                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it.timestamp }
//                        is NoteOrder.Color -> notes.sortedBy { it.color }
                    }
                }
                is OrderType.Descending -> {
                    when(noteOrder) {
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending  { it.timestamp }
//                        is NoteOrder.Color -> notes.sortedByDescending { it.color }
                    }
                }
            }
        }
    }
}