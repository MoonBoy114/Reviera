package com.example.reviera.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val OrangeColorScheme = darkColorScheme(
    primary = Color(0xFFFF5722), // Оранжевый
    secondary = Color(0xFFFF8A50), // Светло-оранжевый
    background = Color(0xFF1C2526), // Темный фон
    surface = Color(0xFF2C3E50), // Серо-синий фон
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White, // Изменено на черный
    onSurface = Color.Black
)

@Composable
fun RivieraSpaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = OrangeColorScheme,
        typography = Typography,
        content = content
    )
}