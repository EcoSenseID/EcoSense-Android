package com.ecosense.android.core.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = DarkGreen,
    primaryVariant = LeafGreen,
    secondary = ElectricGreen,
)

private val LightColorPalette = lightColors(
    primary = DarkGreen,
    primaryVariant = LeafGreen,
    secondary = ElectricGreen,
    background = LightGrey,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

@Composable
fun EcoSenseTheme(
    darkTheme: Boolean = false, /* isSystemInDarkTheme() */
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colors = LightColorPalette /* if (darkTheme) DarkColorPalette else LightColorPalette */,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}