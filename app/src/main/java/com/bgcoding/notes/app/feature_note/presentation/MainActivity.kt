package com.bgcoding.notes.app.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import com.bgcoding.notes.app.ui.theme.AndroidNotesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidNotesAppTheme {
                Text("Hello, AndroidNotesApp!")
            }
        }
    }
}