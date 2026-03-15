package com.example.kakuro_hero.views.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val KakuroDarkScheme = darkColorScheme(
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFE5E7EB),

    surface = Color(0xFF111C33),
    onSurface = Color(0xFFE5E7EB),

    surfaceVariant = Color(0xFF1E2A44),
    onSurfaceVariant = Color(0xFFCBD5E1),

    surfaceTint = Color.Transparent,

    primary = GoldOrange,
    onPrimary = Color(0xFF0B1220),

    primaryContainer = Color(0xFF2B364F),
    onPrimaryContainer = Color(0xFFE5E7EB),

    secondary = DarkOrange,
    onSecondary = Color(0xFF0B1220),

    secondaryContainer = Color(0xFF3A2A1E),
    onSecondaryContainer = Color(0xFFE5E7EB),

    outline = Color(0xFF334155),
)

private val KakuroLightScheme = lightColorScheme(
    // Base
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F172A),

    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF334155),

    surfaceTint = Color.Transparent,


    primary = GoldOrange,
    onPrimary = Color(0xFF0B1220),

    primaryContainer = Color(0xFFFFF4D6),
    onPrimaryContainer = Color(0xFF0F172A),


    secondary = DarkOrange,
    onSecondary = Color(0xFF0B1220),

    secondaryContainer = Color(0xFFFFEAD6),
    onSecondaryContainer = Color(0xFF0F172A),

    outline = Color(0xFFE2E8F0),
)

@Composable
fun Kakuro_Hero_First_PrototypeTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> KakuroDarkScheme
        else -> KakuroLightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}