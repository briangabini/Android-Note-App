package com.bgcoding.notes.app.feature_note.presentation.notes.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bgcoding.notes.app.feature_note.presentation.notes.NotesEvent
import com.bgcoding.notes.app.feature_note.presentation.notes.NotesState
import com.bgcoding.notes.app.feature_note.presentation.notes.NotesViewModel
import com.bgcoding.notes.app.feature_note.presentation.util.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesList(
    state: NotesState,
    showDeleted: Boolean,
    viewModel: NotesViewModel,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    showDateEnabled: Boolean,
    navController: NavController,
) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface)
    ) {
        items(state.notes, key = { it.id!! }) { note ->
            DisposableEffect(Unit) {
                Log.d("LazyColumn", "LazyColumn recomposed")
                onDispose {}
            }
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    if (!showDeleted && it == SwipeToDismissBoxValue.EndToStart) {
                        viewModel.onEvent(NotesEvent.MarkNoteAsDeleted(note))
                        scope.launch {
                            val result = snackbarHostState
                                .showSnackbar(
                                    message = "Note deleted",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = true
                                )
                            when (result) {
                                SnackbarResult.ActionPerformed -> {
                                    viewModel.onEvent(NotesEvent.RestorePreviouslyDeletedNote)
                                }
                                SnackbarResult.Dismissed -> {
                                    // do nothing
                                }
                            }
                        }
                        true

                    } else if (showDeleted && it == SwipeToDismissBoxValue.EndToStart) {
                        viewModel.onEvent(NotesEvent.RestoreDeletedNote(note))
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Note restored",
                                duration = SnackbarDuration.Short
                            )
                        }
                        true
                    } else {
                        false
                    }
                }
            )

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    if (!showDeleted) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.error)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = "Delete",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onError
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = "Restore",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }


                }
            ) {
                NoteItem(
                    note = note,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable {
                            navController.navigate(
                                Screen.AddEditNoteScreen.route +
                                        "?noteId=${note.id}"
                            )
                        },
                    showDate = showDateEnabled
                )
            }

            // only display this if the note is not the last one
            if (state.notes.last() != note)
                HorizontalDivider(
                    thickness = 1.dp,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                )
        }
    }
}