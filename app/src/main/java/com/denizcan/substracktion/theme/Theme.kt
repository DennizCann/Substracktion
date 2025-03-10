package com.denizcan.substracktion.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.denizcan.substracktion.screens.theme.Typography

// Light Theme Colors
private val LightPrimaryColor = Color(0xFF6B4EFF)
private val LightSecondaryColor = Color(0xFF7E6BF6)
private val LightBackgroundColor = Color(0xFFFFFFFF)
private val LightSurfaceColor = Color(0xFFF5F5F5)
private val LightTextPrimaryColor = Color(0xFF1A1A1A)
private val LightTextOnPrimaryColor = Color(0xFFFFFFFF)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimaryColor,
    secondary = LightSecondaryColor,
    background = LightBackgroundColor,
    surface = LightSurfaceColor,
    onPrimary = LightTextOnPrimaryColor,
    onSecondary = LightTextOnPrimaryColor,
    onBackground = LightTextPrimaryColor,
    onSurface = LightTextPrimaryColor
)

@Composable
fun SubstracktionTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,  // Sadece açık tema kullan
        typography = Typography,
        content = content
    )
}