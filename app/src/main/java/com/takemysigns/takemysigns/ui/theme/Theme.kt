package com.takemysigns.takemysigns.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = primaryColor,
    secondary = textColorLight,
    surface = Color.White,
    onPrimary = Color.White,
    onSurface = textColorLight
)

private val DarkColorPalette = darkColors(
    primary = textColorDark,
    secondary = primaryColor,
    surface = Color.White,
    onPrimary = textColorDark,
    onSurface = textColorDark
)

@Composable
fun TakeMySignsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}