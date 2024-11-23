package com.bgcoding.notes.app.feature_note.presentation.settings

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bgcoding.notes.app.di.AppModule
import com.bgcoding.notes.app.feature_note.presentation.notes.NotesEvent
import com.bgcoding.notes.app.feature_note.presentation.notes.NotesViewModel
import com.bgcoding.notes.app.feature_note.presentation.notes.components.OrderSection
import com.bgcoding.notes.app.feature_note.presentation.util.Screen
import com.bgcoding.notes.app.ui.theme.ThemeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.map

import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController,
                   themeViewModel: ThemeViewModel = hiltViewModel(),
                   viewModel: NotesViewModel = hiltViewModel(),
                   dataStore: DataStore<Preferences> = themeViewModel.dataStore
) {
    // get showDeleted from navController argument
    val showDeleted = navController.currentBackStackEntry?.arguments?.getBoolean("showDeleted") ?: false

    LaunchedEffect(showDeleted) {
        viewModel.onEvent(NotesEvent.SetShowDeleted(showDeleted))
        Log.d("NotesScreen", "in LaunchedEffect showDeleted: $showDeleted")
    }

    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()    // scoped to the composable
    val showDialog = remember { mutableStateOf(false) }

    // State variables for toggles
//    val themeViewModel: ThemeViewModel = hiltViewModel()
    val isNightModeEnabled by themeViewModel.isDarkTheme.collectAsState()
    val isShowDateEnabled by viewModel.isShowDateEnabled.collectAsState()


    LaunchedEffect(isNightModeEnabled, isShowDateEnabled) {
        Log.d("SettingsScreen", "isNightModeEnabled: $isNightModeEnabled")
        Log.d("SettingsScreen", "isShowDateEnabled: $isShowDateEnabled")
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(280.dp),
            ) {
                // only span the right width
                Text(
                    "Simple Notes",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Notes,
                            contentDescription = "Notes",
                        )
                    },
                    label = {
                        Text(text = "Notes", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    },
                    selected = !showDeleted,
                    onClick = {
                        navController.navigate(Screen.NotesScreen.route + "?showDeleted=false")
                    }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Trash",
                        )
                    },
                    label = {
                        Text(text = "Bin", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    },
                    selected = showDeleted,
                    onClick = {
                        navController.navigate(Screen.NotesScreen.route + "?showDeleted=true")
                    }
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                        )
                    },
                    label = {
                        Text(text = "Settings", style = MaterialTheme.typography.bodyMedium, fontWeight  = FontWeight.Bold)
                    },
                    selected = false,
                    onClick = { navController.navigate(Screen.SettingsScreen.route) }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {

                        Text(
                            "Settings",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )

                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Open Sidebar Menu (Navigation Drawer)"
                            )
                        }
                    },
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Night Mode Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Night Mode",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = isNightModeEnabled,
                        onCheckedChange = {
                            themeViewModel.toggleTheme(it)
                            scope.launch {
                                dataStore.edit { preferences ->
                                    preferences[booleanPreferencesKey("nightMode")] = it
                                }
                            }
                        }
                    )
                }

                // Show Date Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Show Note Timestamp",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = isShowDateEnabled,
                        onCheckedChange = {
                            viewModel.setShowDate(it)
                            scope.launch {
                                dataStore.edit { preferences ->
                                    preferences[booleanPreferencesKey("showDate")] = it
                                }
                            }
                        }
                    )
                }
            }

        }
    }


}