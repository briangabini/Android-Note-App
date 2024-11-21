package com.bgcoding.notes.app.feature_note.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bgcoding.notes.app.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.bgcoding.notes.app.feature_note.presentation.notes.NotesScreen
import com.bgcoding.notes.app.feature_note.presentation.ocr.CameraScreen
import com.bgcoding.notes.app.feature_note.presentation.settings.SettingsScreen
import com.bgcoding.notes.app.feature_note.presentation.util.Screen
import com.bgcoding.notes.app.ui.theme.AndroidNotesAppTheme
import com.bgcoding.notes.app.ui.theme.ThemeViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get user permissions
        if (!hasRequiredPermissions()) {
            requestPermissions(CAMERAX_PERMISSIONS, 0)
        }

        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            AndroidNotesAppTheme(darkTheme = isDarkTheme) {
                Surface(
                    color = MaterialTheme.colorScheme.surface
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route
                    ) {
                        composable(route = Screen.NotesScreen.route) {
                            NotesScreen(navController = navController)
                        }
                        composable(route = Screen.SettingsScreen.route) {
                            SettingsScreen(navController = navController, themeViewModel = themeViewModel)
                        }
                        composable(
                            route = Screen.AddEditNoteScreen.route +
                                    "?noteId={noteId}",
//                                "?noteId={noteId}&noteColor={noteColor}",
                            // These arguments will be passed to the SavedStateHandle automatically
                            arguments = listOf(
                                navArgument(
                                    name = "noteId"         // the one enclosed in {} is the argument name
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                            )
                        ) {
                            AddEditNoteScreen(
                                navController = navController,
                            )
                        }
                        composable(route = Screen.CameraScreen.route) {
                            CameraScreen(navController = navController)
                        }
                        composable(
                            route = Screen.NotesScreen.route + "?showDeleted={showDeleted}",
                            arguments = listOf(
                                navArgument(
                                    name = "showDeleted"
                                ) {
                                    type = NavType.BoolType
                                    defaultValue = false
                                }
                            )
                        ) {
                            NotesScreen(navController = navController)
                        }
                    }
                }
            }


        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }
}