package com.bgcoding.notes.app.feature_note.presentation.notes

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bgcoding.notes.app.feature_note.domain.model.Note
import com.bgcoding.notes.app.feature_note.domain.use_case.NoteUseCases
import com.bgcoding.notes.app.feature_note.domain.use_case.RetrieveMode
import com.bgcoding.notes.app.feature_note.domain.util.NoteOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null

    // use a coroutine job so that we can cancel it when needed
    private var getNotesJob: Job? = null

    /*init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }*/

    fun onEvent(event: NotesEvent) {
        when(event) {
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.noteOrder::class &&           // same class
                    state.value.noteOrder.orderType == event.noteOrder.orderType) {     // same ordertype between the current state and the event
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(event.note.copy(deleted = true))
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.RestorePreviouslyDeletedNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(
                        recentlyDeletedNote?.copy(deleted = false) ?: return@launch
                    )
                    recentlyDeletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
            is NotesEvent.SetShowDeleted -> {
                _state.value = state.value.copy(showDeleted = event.showDeleted)
                Log.d("NotesViewModel", "SetShowDeleted executed")
                Log.d("NotesViewModel", "from event showDeleted: ${event.showDeleted}")
                Log.d("NotesViewModel", "from state showDeleted: ${_state.value.showDeleted}")
                getNotes(state.value.noteOrder)
            }
            is NotesEvent.DeleteAllNotesPermanently -> {
                viewModelScope.launch {
                    noteUseCases.deleteAllNotesPermanently()
                }
            }
            is NotesEvent.RestoreDeletedNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(
                        event.note.copy(deleted = false)
                    )
                }
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        Log.d("NotesViewModel", "getNotes Executed")
        Log.d("NotesViewModel", "from state showDeleted: ${_state.value.showDeleted}")
        val retrieveMode = if (_state.value.showDeleted) {
            RetrieveMode.ShowDeleted
        } else {
            RetrieveMode.ShowNonDeleted
        }
        Log.d("NotesViewModel", "getNotes called")
        Log.d("NotesViewModel", "retrieveMode: $retrieveMode")

        getNotesJob = noteUseCases.getNotes(noteOrder, retrieveMode).onEach { notes ->
            _state.value = state.value.copy(notes = notes, noteOrder = noteOrder)
            Log.d("NotesViewModel", "getNotesJob executed")
            Log.d("NotesViewModel", "retrieveMode: $retrieveMode")
            Log.d("NotesViewModel", "notes: $notes")
        }.launchIn(viewModelScope)
    }
}