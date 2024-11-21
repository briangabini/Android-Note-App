package com.bgcoding.notes.app.feature_note.presentation.add_edit_note

import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
            if(noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = _noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _deleted.value = note.deleted
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when(event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = _noteContent.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = _noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _noteContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.SaveNote -> {
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
                    } catch(e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
            is AddEditNoteEvent.CopyContent -> {
                viewModelScope.launch {
                    // copy content to clipboard
                    val clipboardManager = event.clipboardManager
                    clipboardManager.setText(AnnotatedString(_noteContent.value.text))
                    // notify user
                    _eventFlow.emit(UiEvent.ShowSnackbar("Content copied to clipboard"))
                }
            }
            is AddEditNoteEvent.DeleteNote -> {
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
            is AddEditNoteEvent.RestoreNote -> {
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
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
    }
}