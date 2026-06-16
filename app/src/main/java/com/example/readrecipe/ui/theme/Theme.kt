package com.example.readrecipe.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = SoftOrange,
    onPrimary = Color.White,
    primaryContainer = SoftOrangeLight,
    onPrimaryContainer = DarkText,
    secondary = SoftOrange,
    onSecondary = Color.White,
    background = Color.White,
    surface = Color.White,
    onBackground = DarkText,
    onSurface = DarkText,
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = DarkText,
    outline = ChipBorder,
)

@Composable
fun ReadRecipeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
