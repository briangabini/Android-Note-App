package com.bgcoding.notes.app.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bgcoding.notes.app.ui.theme.BabyBlue
import com.bgcoding.notes.app.ui.theme.LightGreen
import com.bgcoding.notes.app.ui.theme.RedOrange
import com.bgcoding.notes.app.ui.theme.RedPink
import com.bgcoding.notes.app.ui.theme.Violet

@Entity
data class Note(
    var title: String,
    val content: String,
    val timestamp: Long,
//    val color: Int,
    @PrimaryKey val id: Int? = null
) {
    /*companion object {
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }*/
}

class InvalidNoteException(message: String): Exception(message)
