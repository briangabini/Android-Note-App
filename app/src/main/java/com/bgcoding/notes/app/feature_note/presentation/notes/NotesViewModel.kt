package com.bgcoding.notes.app.feature_note.presentation.notes

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bgcoding.notes.app.feature_note.domain.model.Note
import com.bgcoding.notes.app.feature_note.domain.use_case.NoteUseCases
import com.bgcoding.notes.app.feature_note.domain.use_case.RetrieveMode
import com.bgcoding.notes.app.feature_note.domain.util.NoteOrder
import com.bgcoding.notes.app.feature_note.presentation.notes.NotesEvent.DeleteAllNotesPermanently
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val dataStore: DataStore<Preferences>,
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null

    // use a coroutine job so that we can cancel it when needed
    private var getNotesJob: Job? = null

    // TODO: Might have to move these to a separate viewmodel (settings)
    private var currentSearchQuery: String? = null
    private val _isShowDateEnabled = MutableStateFlow(false)
    val isShowDateEnabled: StateFlow<Boolean> = _isShowDateEnabled

    init {
        viewModelScope.launch {
            dataStore.data.map { preferences ->
                preferences[booleanPreferencesKey("showDate")] ?: false
            }.collect { isShowDate ->
                _isShowDateEnabled.value = isShowDate
            }
        }
    }

    fun onEvent(event: NotesEvent) {
        when(event) {
            is NotesEvent.Order -> setOrderOfNotes(event.noteOrder);
            is NotesEvent.MarkNoteAsDeleted -> markNoteAsDeleted(event.note);
            is NotesEvent.RestorePreviouslyDeletedNote -> restorePreviouslyDeletedNote();
            is NotesEvent.ToggleOrderSection -> toggleOrderSection();
            is NotesEvent.SetShowDeleted -> setShowDeleted(event.showDeleted);
            is DeleteAllNotesPermanently -> deleteAllNotesPermanently();
            is NotesEvent.RestoreDeletedNote -> restoreDeletedNote(event.note);
            is NotesEvent.SearchNotes -> searchNotes(event.query);
        }
    }

    private fun markNoteAsDeleted(note: Note) {
        viewModelScope.launch {
            noteUseCases.addNote(note.copy(deleted = true))
            recentlyDeletedNote = note
        }
    }

    private fun restorePreviouslyDeletedNote() {
        viewModelScope.launch {
            noteUseCases.addNote(
                recentlyDeletedNote?.copy(deleted = false) ?: return@launch
            )
            recentlyDeletedNote = null
        }
    }

    private fun toggleOrderSection() {
        _state.value = state.value.copy(
            isOrderSectionVisible = !state.value.isOrderSectionVisible
        )
    }

    private fun setShowDeleted(showDeleted: Boolean) {
        _state.value = state.value.copy(showDeleted = showDeleted)
        getNotes(state.value.noteOrder)
    }

    private fun deleteAllNotesPermanently() {
        viewModelScope.launch {
            noteUseCases.deleteAllNotesPermanently()
        }
    }

    private fun restoreDeletedNote(note: Note) {
        viewModelScope.launch {
            noteUseCases.addNote(
                note.copy(deleted = false)
            )
        }
    }

    private fun searchNotes(query: String) {
        currentSearchQuery = query
        getNotes(state.value.noteOrder, query)
    }

    private fun setOrderOfNotes(noteOrder: NoteOrder) {
        if (state.value.noteOrder::class == noteOrder::class &&           // same class
            state.value.noteOrder.orderType == noteOrder.orderType) {     // same ordertype between the current state and the event
            return
        }
        getNotes(noteOrder, currentSearchQuery)
    }

    private fun getNotes(noteOrder: NoteOrder, query: String? = null) {
        getNotesJob?.cancel()
        val retrieveMode = if (_state.value.showDeleted) {
            RetrieveMode.ShowDeleted
        } else {
            RetrieveMode.ShowNonDeleted
        }

        getNotesJob = noteUseCases.getNotes(noteOrder, retrieveMode, query).onEach { notes ->
            _state.value = state.value.copy(notes = notes, noteOrder = noteOrder)
        }.launchIn(viewModelScope)
    }

    // TODO: Move to a separate viewmodel, possibly for settings
    fun setShowDate(isShowDate: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[booleanPreferencesKey("showDate")] = isShowDate
            }
            _isShowDateEnabled.value = isShowDate
        }
    }
}