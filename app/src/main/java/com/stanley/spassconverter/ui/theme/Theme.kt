package com.stanley.spassconverter.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BtnPrimaryBg,
    onPrimary = BtnPrimaryText,
    primaryContainer = BgSuccessZone,
    onPrimaryContainer = TextSuccessTitle,
    surface = BgSurface,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = BorderDefault,
    errorContainer = Color(0xFFFDEAE8),
    onErrorContainer = Color(0xFFB3261E)
)

@Composable
fun SpassConverterTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}