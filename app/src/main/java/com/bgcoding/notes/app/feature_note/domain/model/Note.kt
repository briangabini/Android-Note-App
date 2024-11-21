package com.bgcoding.notes.app.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    var title: String,
    val content: String,
    val timestamp: Long,
    var deleted: Boolean = false,
    @PrimaryKey val id: Int? = null
) {
}

class InvalidNoteException(message: String): Exception(message)
