package com.bgcoding.notes.app.feature_note.presentation.add_edit_note.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bgcoding.notes.app.feature_note.presentation.add_edit_note.AddEditNoteEvent
import com.bgcoding.notes.app.feature_note.presentation.add_edit_note.AddEditNoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteTopBar(
    navController: NavController,
    viewModel: AddEditNoteViewModel,
    dropdownMenuExpanded: MutableState<Boolean>,
    showDeleteNoteDialog: MutableState<Boolean>,
    showRestoreNoteDialog: MutableState<Boolean>,
    clipboardManager: ClipboardManager,
    context: Context
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {},
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Save note",
                    modifier = Modifier.padding(8.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = { if (!viewModel.deleted.value) navController.navigate("camera_screen") }) {
                if (!viewModel.deleted.value)
                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = "Open camera"
                    )
            }
            IconButton(onClick = { dropdownMenuExpanded.value = true }) {
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
                        dropdownMenuExpanded.value = false
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "${viewModel.noteTitle.value.text}\n${viewModel.noteContent.value.text}")
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
                        showDeleteNoteDialog.value = true
                        dropdownMenuExpanded.value = false
                    }
                )
            }
        }
    )
}