package com.bgcoding.notes.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color.White,
    background = DarkGray,
    onBackground = Color.White,
    surface = LightBlue,
    onSurface = DarkGray
)

private val LightColorScheme = lightColorScheme(
    primary = Color.Black,
    background = Color.White,
    onBackground = DarkGray,
    surface = LightBlue,
    onSurface = Color.Black
)

@Composable
fun AndroidNotesAppTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    MaterialTheme(
//        colorScheme = DarkColorScheme,
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

