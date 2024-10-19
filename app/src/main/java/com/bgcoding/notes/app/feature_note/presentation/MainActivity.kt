package com.bgcoding.notes.app.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bgcoding.notes.app.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.bgcoding.notes.app.feature_note.presentation.notes.NotesScreen
import com.bgcoding.notes.app.feature_note.presentation.util.Screen
import com.bgcoding.notes.app.ui.theme.AndroidNotesAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidNotesAppTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route
                    ) {
                        composable(route = Screen.NotesScreen.route) {
                            NotesScreen(navController = navController)
                        }
                        composable(
                            route = Screen.AddEditNoteScreen.route +
//                                "?noteId={noteId}&noteColor={noteColor}",
                                    "?noteId={noteId}",
                            arguments = listOf(
                                navArgument(
                                    name = "noteId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                /*navArgument(
                                    name = "noteColor"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }*/
                            )
                        ) {
                            val color = it.arguments?.getInt("NoteColor") ?: -1
                            /*AddEditNoteScreen(
                                navController = navController,
                                noteColor = color
                            )*/
                            AddEditNoteScreen(
                                navController = navController,
                            )
                        }
                    }
                }
            }
        }
    }
}