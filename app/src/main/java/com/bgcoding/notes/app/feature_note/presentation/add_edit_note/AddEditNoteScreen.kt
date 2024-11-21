package com.bgcoding.notes.app.feature_note.presentation.add_edit_note

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bgcoding.notes.app.feature_note.presentation.add_edit_note.components.TransparentHintTextField
import com.bgcoding.notes.app.feature_note.presentation.notes.NotesEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
//    noteColor: Int,
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

    // Handle quit app
    DisposableEffect(Unit) {
        onDispose {
            if (!viewModel.deleted.value) {
                viewModel.onEvent(AddEditNoteEvent.SaveNote)
            }
        }
    }

    if (showRestoreNoteDialog.value) {
        BasicAlertDialog(
            onDismissRequest = { showRestoreNoteDialog.value = false }
        ) {
            Surface(
                modifier = Modifier
                    .padding(16.dp),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(
                    modifier = Modifier.wrapContentSize()
                ) {
                    Text(
                        text = "Restore Note",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Are you sure you want to restore this note?",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        TextButton(
                            onClick = {
                                showRestoreNoteDialog.value = false
                                dropdownMenuExpanded.value = false
                            }
                        ) {
                            Text(
                                "Cancel",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = {
                                viewModel.onEvent(AddEditNoteEvent.RestoreNote)
                                showRestoreNoteDialog.value = false
                                dropdownMenuExpanded.value = false
                                navController.navigateUp()
                            }
                        ) {
                            Text(
                                "Restore",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                    }
                }
            }
        }
    }

    if (showDeleteNoteDialog.value) {
        BasicAlertDialog(
            onDismissRequest = { showDeleteNoteDialog.value = false }
        ) {
            Surface(
                modifier = Modifier
                    .padding(16.dp),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(
                    modifier = Modifier.wrapContentSize()
                ) {
                    if (viewModel.deleted.value) {
                        Text(
                            text = "Delete Note",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Are you sure you want to delete this note?",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            text = "Move to Trash?",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Are you sure you want to move this note to trash?",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 16.dp
                                ),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                        TextButton(
                            onClick = {
                                showDeleteNoteDialog.value = false
                                dropdownMenuExpanded.value = false
                            }
                        ) {
                            Text(
                                "Cancel",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = {
                                viewModel.onEvent(AddEditNoteEvent.DeleteNote)
                                showDeleteNoteDialog.value = false
                                dropdownMenuExpanded.value = false
                                navController.navigateUp()
                            }
                        ) {
                            Text(
                                "Delete",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Save note",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate("camera_screen")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = "Open camera"
                        )
                    }
                    IconButton(
                        onClick = {
                            dropdownMenuExpanded.value = true
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options"
                        )
                    }
                    DropdownMenu(
                        expanded = dropdownMenuExpanded.value,
                        onDismissRequest = { dropdownMenuExpanded.value = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text("Copy") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy note"
                                )
                            },
                            onClick = {
                                viewModel.onEvent(AddEditNoteEvent.CopyContent(clipboardManager))
                                // close dropdown
                                dropdownMenuExpanded.value = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Share") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share note"
                                )
                            },
                            onClick = {
                                // close dropdown
                                dropdownMenuExpanded.value = false
                                // create an implicit intent to share the note
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, "${titleState.text}\n${contentState.text}")
                                }
                                val chooser = Intent.createChooser(intent, "Share Note")
                                context.startActivity(chooser)
                            }
                        )
                        HorizontalDivider(thickness = 1.dp)
                        if (viewModel.deleted.value) {
                            DropdownMenuItem(
                                text = { Text("Restore") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.RestoreFromTrash,
                                        contentDescription = "Restore Note"
                                    )
                                },
                                onClick = {
                                    showRestoreNoteDialog.value = true
                                    // close dropdown
                                    dropdownMenuExpanded.value = false
                                }
                            )
                        }
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Copy note"
                                )
                            },
                            onClick = {
                                // close dropdown
                                showDeleteNoteDialog.value = true
                                dropdownMenuExpanded.value = false
                            }
                        )
                    }
                }
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
                textStyle = MaterialTheme.typography.titleLarge
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
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}