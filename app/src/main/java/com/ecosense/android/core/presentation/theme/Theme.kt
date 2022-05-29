package com.ecosense.android.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Teal200,
    primaryVariant = Teal700,
    secondary = Lime200
)

private val LightColorPalette = lightColors(
    primary = Teal800,
    primaryVariant = Teal500,
    secondary = Lime600,
    background = Gray50,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Gray900,
    onSurface = Gray900,
)

@Composable
fun EcoSenseTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()

    systemUiController.setSystemBarsColor(color = if (darkTheme) Color.Black else Color.White)

    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colors = if (darkTheme) DarkColorPalette else LightColorPalette,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}