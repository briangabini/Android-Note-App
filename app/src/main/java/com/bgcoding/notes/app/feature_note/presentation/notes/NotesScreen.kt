package com.bgcoding.notes.app.feature_note.presentation.notes

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bgcoding.notes.app.feature_note.presentation.notes.components.AppDrawerContent
import com.bgcoding.notes.app.feature_note.presentation.notes.components.DeleteBinDialog
import com.bgcoding.notes.app.feature_note.presentation.notes.components.NotesList
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
    val showDateEnabled = viewModel.isShowDateEnabled.collectAsState()

    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()    // scoped to the composable
    val searchQuery = remember { mutableStateOf("") }
    val showSearchField = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val maxChar = 20

    if (showDialog.value) {
        DeleteBinDialog(
            viewModel = viewModel,
            showDialog = showDialog
        )
    }

    // Log recompositions
    val currentState = rememberUpdatedState(state)
    DisposableEffect(currentState) {
        Log.d("NotesScreen", "Recomposed with state: $currentState")
        onDispose { }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            AppDrawerContent(
                showDeleted = showDeleted,
                navController = navController
            )
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
                        if (showSearchField.value) {
                            BasicTextField(
                                value = searchQuery.value,
                                onValueChange = {
                                    if (it.length <= maxChar) {
                                        searchQuery.value = it
                                        viewModel.onEvent(NotesEvent.SearchNotes(it))
                                    }
                                },
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                MaterialTheme.colorScheme.surface,
                                                MaterialTheme.shapes.small
                                            )
                                            .padding(8.dp)
                                    ) {
                                        if (searchQuery.value.isEmpty()) {
                                            Text("Search notes", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                                        }
                                        innerTextField()
                                    }
                                },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            if(showDeleted){
                                Text("Bin", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            } else {
                                Text("Notes", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            }
                        }
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
                                showSearchField.value = !showSearchField.value
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
                NotesList(
                    state = state,
                    showDeleted = showDeleted,
                    viewModel = viewModel,
                    scope = scope,
                    snackbarHostState = snackbarHostState,
                    showDateEnabled = showDateEnabled.value,
                    navController = navController
                )
            }
        }
    }


}