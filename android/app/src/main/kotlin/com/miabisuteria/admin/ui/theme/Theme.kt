package com.miabisuteri.admin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AdminColorScheme = darkColorScheme(
    primary           = VerdeClaro,
    onPrimary         = Color.White,
    primaryContainer  = Color(0xFF1E3A0F),
    onPrimaryContainer= VerdeMenta,
    secondary         = Oro,
    onSecondary       = Color(0xFF1A1000),
    secondaryContainer= Color(0xFF2A1E00),
    onSecondaryContainer = Oro2,
    background        = AdminBackground,
    onBackground      = TextoPrimario,
    surface           = AdminSurface,
    onSurface         = TextoPrimario,
    surfaceVariant    = AdminCard,
    onSurfaceVariant  = TextoSecundario,
    outline           = AdminBorder,
    error             = Color(0xFFCF6679),
    onError           = Color.White,
)

@Composable
fun MiaAdminTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AdminColorScheme,
        typography = MiaTypography,
        content = content
    )
}
