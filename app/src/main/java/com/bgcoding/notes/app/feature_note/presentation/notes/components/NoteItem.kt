package com.bgcoding.notes.app.feature_note.presentation.notes.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bgcoding.notes.app.feature_note.domain.model.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

@SuppressLint("SimpleDateFormat")
@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    showDate: Boolean
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(end = 32.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            // if content is empty do not display
            if (note.content.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                )
            }
//            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
//            formatter.timeZone = TimeZone.getTimeZone("GMT+8")
//
//            val dateString = formatter.format(Date(note.timestamp))
//            if (showDate) {
//                Text(
//                    text = dateString,
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.onSurface,
//                )
//            }
            val currentTime = System.currentTimeMillis()
            val timeDifference = currentTime - note.timestamp

            val dateString = when {
                timeDifference <= 5 * 1000 -> "Just Now"
                timeDifference > 5 * 1000 && timeDifference < 60 * 1000 -> "${timeDifference / 1000} seconds ago"
                timeDifference < 2 * 60 * 1000 -> "a minute ago"
                timeDifference < 60 * 60 * 1000 -> "${timeDifference / (60 * 1000)} minutes ago"
                timeDifference < 2 * 60 * 60 * 1000 -> "an hour ago"
                timeDifference < 24 * 60 * 60 * 1000 -> "${timeDifference / (60 * 60 * 1000)} hours ago"
                else -> {
                    val formatter = SimpleDateFormat("dd/MM HH:mm") // should change to shorthand month
                    formatter.timeZone = TimeZone.getTimeZone("GMT+8")
                    formatter.format(Date(note.timestamp))
                }
            }

            if (showDate) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = dateString,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 10.sp
                )
            }
        }

    }
}