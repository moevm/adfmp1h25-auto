package com.example.testauto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.testauto.navigation.MyApp
import com.example.testauto.ui.theme.TestAutoTheme // Импортируем кастомную тему

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