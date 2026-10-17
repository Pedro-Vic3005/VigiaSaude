package com.example.modelobase.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Palette = lightColorScheme(
    primary = Teal,
    onPrimary = Color.White,
    primaryContainer = Mint,
    onPrimaryContainer = Navy,
    secondary = Orange,
    onSecondary = Navy,
    tertiary = Wine,
    onTertiary = Color.White,
    background = Cream,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Mint,
    onSurfaceVariant = Muted,
    outline = Border,
    error = ErrorRed
)

private val VigiaTypography = Typography(
    headlineLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 31.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 35.sp),
    headlineMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 26.sp, fontWeight = FontWeight.Bold, lineHeight = 31.sp),
    titleLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 22.sp, fontWeight = FontWeight.Bold, lineHeight = 27.sp),
    titleMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 17.sp, fontWeight = FontWeight.Bold, lineHeight = 24.sp),
    bodyLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 14.sp, lineHeight = 21.sp),
    labelLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 15.sp, fontWeight = FontWeight.Bold)
)

@Composable
fun VigiaSaudeTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Palette, typography = VigiaTypography, content = content)
}
