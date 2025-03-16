package com.example.testauto.navigation

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testauto.components.BottomNavigationBar
import com.example.testauto.models.MaintenanceLog
import com.example.testauto.models.Reminder
import com.example.testauto.models.Screen
import com.example.testauto.models.SurveyData
import com.example.testauto.screens.HomeScreen
import com.example.testauto.screens.MaintenanceScreen
import com.example.testauto.screens.ReminderScreen
import com.example.testauto.screens.SurveyScreen
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Locale

const val SURVEY_ROUTE = "survey"
const val PREFS_NAME = "ReminderPrefs"
const val KEY_REMINDERS = "reminders"

// Функция для преобразования даты из формата гггг-мм-дд в дд.мм.гггг
fun convertToRussianDateFormat(dateStr: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateStr)
        outputFormat.format(date)
    } catch (e: Exception) {
        dateStr
    }
}



@Composable
fun MyApp() {
    val navController = rememberNavController()
    val bottomNavItems = listOf(Screen.Home, Screen.Reminders, Screen.Maintenance)

    // Получаем контекст для SharedPreferences
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val gson = Gson()

    // Загружаем напоминания из SharedPreferences или используем начальные данные
    val savedRemindersJson = prefs.getString(KEY_REMINDERS, null)
    val remindersState = remember {
        mutableStateOf(
            if (savedRemindersJson != null) {
                val type = object : TypeToken<List<Reminder>>() {}.type
                gson.fromJson(savedRemindersJson, type) ?: initialReminders
            } else {
                initialReminders
            }
        )
    }

    // Функция для сохранения напоминаний в SharedPreferences
    fun saveReminders(reminders: List<Reminder>) {
        with(prefs.edit()) {
            putString(KEY_REMINDERS, gson.toJson(reminders))
            apply()
        }
    }

    // Сохраняем напоминания при каждом изменении remindersState
    LaunchedEffect(remindersState.value) {
        saveReminders(remindersState.value)
    }

    val maintenanceLogsState = remember {
        mutableStateOf(
            listOf(
                MaintenanceLog(
                    "Обслуживание коробки передач",
                    "Замена",
                    convertToRussianDateFormat("2025-03-05"),
                    "10000"
                ),
                MaintenanceLog(
                    "Обслуживание дисков",
                    "Покупка",
                    convertToRussianDateFormat("2025-03-06"),
                    "8000"
                ),
                MaintenanceLog(
                    "Обслуживание датчиков",
                    "Замена",
                    convertToRussianDateFormat("2025-03-07"),
                    "3000"
                )
            )
        )
    }
    val surveyDataState = remember { mutableStateOf<SurveyData?>(null) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, bottomNavItems) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController, remindersState.value, maintenanceLogsState.value) }
            composable(Screen.Reminders.route) { ReminderScreen(navController, remindersState) }
            composable(Screen.Maintenance.route) { MaintenanceScreen(navController, maintenanceLogsState) }
            composable(SURVEY_ROUTE) { SurveyScreen(navController, surveyDataState) }
        }
    }
}