package ru.etu.auto.navigation

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
import ru.etu.auto.components.BottomNavigationBar
import ru.etu.auto.models.MaintenanceLog
import ru.etu.auto.models.Reminder
import ru.etu.auto.models.Screen
import ru.etu.auto.models.SurveyData
import ru.etu.auto.screens.HomeScreen
import ru.etu.auto.screens.MaintenanceScreen
import ru.etu.auto.screens.ReminderScreen
import ru.etu.auto.screens.SurveyScreen
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Locale

const val SURVEY_ROUTE = "survey"
const val PREFS_NAME = "ReminderPrefs"
const val KEY_REMINDERS = "reminders"
const val KEY_MAINTENANCE_LOGS = "maintenance_logs"
const val KEY_COMPLETED_REMINDERS = "completed_reminders" // Новый ключ для выполненных напоминаний

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

// Начальные захардкоженные данные для напоминаний
val initialReminders = listOf(
    Reminder(
        "Аккумулятор",
        convertToRussianDateFormat("2025-03-17"),
        convertToRussianDateFormat("2025-03-17"),
        "Аккумулятор не держит заряд, требуется проверка и возможная замена.",
        15000
    ),
    Reminder(
        "Низкий уровень масла",
        convertToRussianDateFormat("2025-03-10"),
        convertToRussianDateFormat("2025-03-20"),
        "Масло почти на минимуме – проверить утечки и долить масло.",
        10000
    ),
    Reminder(
        "Давление передней правой шины",
        convertToRussianDateFormat("2025-03-10"),
        convertToRussianDateFormat("2025-04-01"),
        "Давление в шине ниже нормы, необходимо проверить и подкачать шину.",
        20000
    ),
    Reminder(
        "Подвеска вышла из строя",
        convertToRussianDateFormat("2025-03-10"),
        convertToRussianDateFormat("2025-03-10"),
        "Требуется обслуживание или замена подвески.",
        23000
    )
)

// Начальные данные для MaintenanceScreen
val initialMaintenanceLogs = listOf(
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

    // Загружаем записи обслуживания из SharedPreferences или используем начальные данные
    val savedMaintenanceLogsJson = prefs.getString(KEY_MAINTENANCE_LOGS, null)
    val maintenanceLogsState = remember {
        mutableStateOf(
            if (savedMaintenanceLogsJson != null) {
                val type = object : TypeToken<List<MaintenanceLog>>() {}.type
                gson.fromJson(savedMaintenanceLogsJson, type) ?: initialMaintenanceLogs
            } else {
                initialMaintenanceLogs
            }
        )
    }

    // Функция для сохранения записей обслуживания в SharedPreferences
    fun saveMaintenanceLogs(logs: List<MaintenanceLog>) {
        with(prefs.edit()) {
            putString(KEY_MAINTENANCE_LOGS, gson.toJson(logs))
            apply()
        }
    }

    // Сохраняем записи обслуживания при каждом изменении maintenanceLogsState
    LaunchedEffect(maintenanceLogsState.value) {
        saveMaintenanceLogs(maintenanceLogsState.value)
    }

    // Загружаем выполненные напоминания из SharedPreferences или используем пустой список
    val savedCompletedRemindersJson = prefs.getString(KEY_COMPLETED_REMINDERS, null)
    val completedRemindersState = remember {
        mutableStateOf<List<Pair<String, Int>>>(
            if (savedCompletedRemindersJson != null) {
                val type = object : TypeToken<List<Pair<String, Int>>>() {}.type
                gson.fromJson(savedCompletedRemindersJson, type) ?: emptyList()
            } else {
                emptyList()
            }
        )
    }

    // Функция для сохранения выполненных напоминаний в SharedPreferences
    fun saveCompletedReminders(completedReminders: List<Pair<String, Int>>) {
        with(prefs.edit()) {
            putString(KEY_COMPLETED_REMINDERS, gson.toJson(completedReminders))
            apply()
        }
    }

    // Сохраняем выполненные напоминания при каждом изменении completedRemindersState
    LaunchedEffect(completedRemindersState.value) {
        saveCompletedReminders(completedRemindersState.value)
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
            composable(Screen.Home.route) {
                HomeScreen(navController, remindersState, maintenanceLogsState.value, surveyDataState, completedRemindersState)
            }
            composable(Screen.Reminders.route) {
                ReminderScreen(navController, remindersState, surveyDataState, completedRemindersState)
            }
            composable(Screen.Maintenance.route) { MaintenanceScreen(navController, maintenanceLogsState) }
            composable(SURVEY_ROUTE) { SurveyScreen(navController, surveyDataState) }
        }
    }
}