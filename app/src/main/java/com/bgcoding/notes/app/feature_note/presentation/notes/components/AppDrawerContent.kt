package com.bgcoding.notes.app.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bgcoding.notes.app.feature_note.presentation.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawerContent(
    showDeleted: Boolean,
    navController: NavController
) {
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
}}