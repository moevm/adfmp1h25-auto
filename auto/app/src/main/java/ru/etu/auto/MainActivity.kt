package ru.etu.auto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.etu.auto.shared.TestAutoTheme
import ru.etu.auto.navigation.MyApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestAutoTheme { // Оборачиваем MyApp в кастомную тему
                MyApp()
            }
        }
    }
}