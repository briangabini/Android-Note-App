package com.bgcoding.notes.app.feature_note.presentation.add_edit_note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bgcoding.notes.app.feature_note.presentation.add_edit_note.components.AddEditNoteTopBar
import com.bgcoding.notes.app.feature_note.presentation.add_edit_note.components.DeleteNoteDialog
import com.bgcoding.notes.app.feature_note.presentation.add_edit_note.components.RestoreNoteDialog
import com.bgcoding.notes.app.feature_note.presentation.add_edit_note.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val dropdownMenuExpanded = remember { mutableStateOf(false) }

    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    // for dialogs
    val showDeleteNoteDialog = remember { mutableStateOf(false) }
    val showRestoreNoteDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (!viewModel.deleted.value) {
                viewModel.onEvent(AddEditNoteEvent.SaveNote)
            }
        }
    }

    if (showRestoreNoteDialog.value) {
        RestoreNoteDialog(
            onDismiss = { showRestoreNoteDialog.value = false },
            onConfirm = {
                viewModel.onEvent(AddEditNoteEvent.RestoreNote)
                showRestoreNoteDialog.value = false
                dropdownMenuExpanded.value = false
                navController.navigateUp()
            }
        )
    }

    if (showDeleteNoteDialog.value) {
        DeleteNoteDialog(
            isDeleted = viewModel.deleted.value,
            onDismiss = { showDeleteNoteDialog.value = false },
            onConfirm = {
                viewModel.onEvent(AddEditNoteEvent.DeleteNote)
                showDeleteNoteDialog.value = false
                dropdownMenuExpanded.value = false
                navController.navigateUp()
            }
        )
    }

    Scaffold(
        topBar = {
            AddEditNoteTopBar(
                navController = navController,
                viewModel = viewModel,
                dropdownMenuExpanded = dropdownMenuExpanded,
                showDeleteNoteDialog = showDeleteNoteDialog,
                showRestoreNoteDialog = showRestoreNoteDialog,
                clipboardManager = clipboardManager,
                context = context
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                enabled = !viewModel.deleted.value
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                },
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                enabled = !viewModel.deleted.value,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}