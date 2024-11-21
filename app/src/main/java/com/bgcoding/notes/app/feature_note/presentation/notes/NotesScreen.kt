package com.bgcoding.notes.app.feature_note.presentation.notes

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bgcoding.notes.app.feature_note.presentation.notes.components.NoteItem
import com.bgcoding.notes.app.feature_note.presentation.notes.components.OrderSection
import com.bgcoding.notes.app.feature_note.presentation.util.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel(),
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

    if (showDialog.value) {
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

    // Log recompositions
    val currentState = rememberUpdatedState(state)
    DisposableEffect(currentState) {
        Log.d("NotesScreen", "Recomposed with state: $currentState")
        onDispose { }
    }



    Log.d("NotesScreen", "showDeleted: $showDeleted")

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
                    onClick = { /*TODO*/ }
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
                        Text("Notes")
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
                    actions = {

                        IconButton(
                            onClick = {
                                viewModel.onEvent(NotesEvent.ToggleOrderSection)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                contentDescription = "Sort notes"
                            )
                        }
                        IconButton(
                            onClick = {
                                // TODO: implement search functionality
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search notes"
                            )
                        }
                    },
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick =
                {
                    if (!showDeleted) {
                        navController.navigate(Screen.AddEditNoteScreen.route)
                    } else {
                        showDialog.value = true
                    }
                },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ) {
                    if (!showDeleted)
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add note")
                    else {
                        Icon(imageVector = Icons.Filled.DeleteForever, contentDescription = "Delete Notes")
                    }
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedVisibility(
                    visible = state.isOrderSectionVisible,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    OrderSection(
                        modifier = Modifier
                            .fillMaxWidth(),
                        noteOrder = state.noteOrder,
                        onOrderChange = {
                            viewModel.onEvent(NotesEvent.Order(it))
                        }
                    )
                }
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                ) {
                    items(state.notes, key = { it.id!! }) { note ->
                        DisposableEffect(Unit) {
                            Log.d("LazyColumn", "LazyColumn recomposed")
                            onDispose {}
                        }
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = {
                                if (!showDeleted && it == SwipeToDismissBoxValue.EndToStart) {
                                    viewModel.onEvent(NotesEvent.DeleteNote(note))
                                    scope.launch {
                                        val result = snackbarHostState
                                            .showSnackbar(
                                                message = "Note deleted",
                                                actionLabel = "Undo",
                                                duration = SnackbarDuration.Short,
                                                withDismissAction = true
                                            )
                                        when (result) {
                                            SnackbarResult.ActionPerformed -> {
                                                viewModel.onEvent(NotesEvent.RestorePreviouslyDeletedNote)
                                            }
                                            SnackbarResult.Dismissed -> {
                                                // do nothing
                                            }
                                        }
                                    }
                                    true

                                } else if (showDeleted && it == SwipeToDismissBoxValue.EndToStart) {
                                    viewModel.onEvent(NotesEvent.RestoreDeletedNote(note))
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Note restored",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                    true
                                } else {
                                    false
                                }
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            backgroundContent = {
                                if (!showDeleted) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(MaterialTheme.colorScheme.error)
                                            .padding(horizontal = 20.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Text(
                                            text = "Delete",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onError
                                        )
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(MaterialTheme.colorScheme.secondaryContainer)
                                            .padding(horizontal = 20.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Text(
                                            text = "Restore",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }


                            }
                        ) {
                            NoteItem(
                                note = note,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface)
                                    .clickable {
                                        navController.navigate(
                                            Screen.AddEditNoteScreen.route +
                                                    "?noteId=${note.id}"
                                        )
                                    }
                            )
                        }

                        // only display this if the note is not the last one
                        if (state.notes.last() != note)
                            HorizontalDivider(
                                thickness = 1.dp,
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                            )
                    }
                }
            }
        }
    }


}