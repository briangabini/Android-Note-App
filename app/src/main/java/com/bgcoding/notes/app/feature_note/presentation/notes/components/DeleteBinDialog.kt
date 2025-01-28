package com.bgcoding.notes.app.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bgcoding.notes.app.feature_note.presentation.notes.NotesEvent
import com.bgcoding.notes.app.feature_note.presentation.notes.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBinDialog(
    viewModel: NotesViewModel,
    showDialog: MutableState<Boolean>
) {
    BasicAlertDialog(
        onDismissRequest = { showDialog.value = false }
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
                    text = "Delete Notes",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Are you sure you want to permanently delete all notes?",
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
                            showDialog.value = false
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
                            showDialog.value = false
                            viewModel.onEvent(NotesEvent.DeleteAllNotesPermanently)
                        }
                    ) {
                        Text(
                            "Delete",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}