package ru.etu.auto.shared

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors // Используем lightColors вместо darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import ru.etu.auto.R

@Composable
fun getColorFromResources(colorResId: Int): Color {
    val context = LocalContext.current
    return Color(ContextCompat.getColor(context, colorResId))
}

@Composable
fun TestAutoTheme(content: @Composable () -> Unit) {
    val mainColor = getColorFromResources(R.color.main_color)
    val subMainColor = getColorFromResources(R.color.sub_main_color)
    val green = getColorFromResources(R.color.green)
    val darkRed = getColorFromResources(R.color.dark_red)
    val red = getColorFromResources(R.color.red)
    val yellow = getColorFromResources(R.color.yellow)
    val white = getColorFromResources(R.color.white)
    val black = getColorFromResources(R.color.black)

    val colors = lightColors(
        primary = mainColor, // #001F54
        primaryVariant = mainColor,
        secondary = green, // #62B65D
        secondaryVariant = green,
        background = white, // Фон белый
        surface = mainColor, // Карточки остаются #001F54
        onPrimary = white, // Текст на кнопках белый (так как primary тёмный)
        onSecondary = white, // Текст на вторичном цвете белый
        onBackground = black, // Текст на фоне чёрный (для контраста с белым фоном)
        onSurface = white, // Текст на карточках белый (так как surface тёмный)
        error = red // #AD2831 для ошибок
    )

    MaterialTheme(
        colors = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}