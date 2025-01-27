package com.bgcoding.notes.app.feature_note.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bgcoding.notes.app.feature_note.domain.model.InvalidNoteException
import com.bgcoding.notes.app.feature_note.domain.model.Note
import com.bgcoding.notes.app.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle,          // this is used to get the noteId from the navigation, injected by hilt
) : ViewModel() {

    private val _noteTitle = mutableStateOf(NoteTextFieldState(
        hint = "Enter title..."
    ))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(
        hint = "Enter some content"
    ))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    private var _deleted = mutableStateOf(false)
    val deleted: State<Boolean> = _deleted

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(text = note.title, isHintVisible = false)
                        _noteContent.value = _noteContent.value.copy(text = note.content, isHintVisible = false)
                        _deleted.value = note.deleted
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> updateTitle(event.value)
            is AddEditNoteEvent.ChangeTitleFocus -> changeTitleFocus(event.focusState)
            is AddEditNoteEvent.EnteredContent -> updateContent(event.value)
            is AddEditNoteEvent.ChangeContentFocus -> changeContentFocus(event.focusState)
            is AddEditNoteEvent.SaveNote -> saveNote()
            is AddEditNoteEvent.CopyContent -> copyContent(event.clipboardManager)
            is AddEditNoteEvent.DeleteNote -> deleteNote()
            is AddEditNoteEvent.RestoreNote -> restoreNote()
        }
    }

    private fun updateTitle(value: String) {
        _noteTitle.value = noteTitle.value.copy(text = value)
    }

    private fun changeTitleFocus(focusState: FocusState) {
        _noteTitle.value = noteTitle.value.copy(
            isHintVisible = !focusState.isFocused && noteTitle.value.text.isBlank()
        )
    }

    private fun updateContent(value: String) {
        _noteContent.value = _noteContent.value.copy(text = value)
    }

    private fun changeContentFocus(focusState: FocusState) {
        _noteContent.value = _noteContent.value.copy(
            isHintVisible = !focusState.isFocused && _noteContent.value.text.isBlank()
        )
    }

    private fun saveNote() {
        viewModelScope.launch {
            try {
                noteUseCases.addNote(
                    Note(
                        title = noteTitle.value.text,
                        content = noteContent.value.text.trimEnd(),
                        timestamp = System.currentTimeMillis(),
                        id = currentNoteId
                    )
                )
            } catch (e: InvalidNoteException) {
                _eventFlow.emit(UiEvent.ShowSnackbar(e.message ?: "Couldn't save note"))
            }
        }
    }

    private fun copyContent(clipboardManager: androidx.compose.ui.platform.ClipboardManager) {
        viewModelScope.launch {
            clipboardManager.setText(AnnotatedString(_noteContent.value.text))
            _eventFlow.emit(UiEvent.ShowSnackbar("Content copied to clipboard"))
        }
    }

    private fun deleteNote() {
        if (currentNoteId == null) return

        viewModelScope.launch {
            if (_deleted.value) {
                noteUseCases.deleteNote(
                    Note(
                        title = noteTitle.value.text,
                        content = noteContent.value.text.trimEnd(),
                        timestamp = System.currentTimeMillis(),
                        id = currentNoteId,
                        deleted = true
                    )
                )
            } else {
                noteUseCases.addNote(
                    Note(
                        title = noteTitle.value.text,
                        content = noteContent.value.text.trimEnd(),
                        timestamp = System.currentTimeMillis(),
                        id = currentNoteId,
                        deleted = true
                    )
                )
                _deleted.value = true
            }
        }
    }

    private fun restoreNote() {
        if (currentNoteId == null) return

        viewModelScope.launch {
            noteUseCases.addNote(
                Note(
                    title = noteTitle.value.text,
                    content = noteContent.value.text.trimEnd(),
                    timestamp = System.currentTimeMillis(),
                    id = currentNoteId,
                    deleted = false
                )
            )
            _deleted.value = false
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
    }
}