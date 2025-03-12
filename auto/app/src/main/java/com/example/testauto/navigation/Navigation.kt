package com.example.testauto.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
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

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val bottomNavItems = listOf(Screen.Home, Screen.Reminders, Screen.Maintenance)

    // Глобальные состояния
    val remindersState = remember {
        mutableStateOf(
            listOf(
                Reminder("Аккумулятор", "2025-03-10", "2025-04-01", "Аккумулятор не держит заряд, требуется проверка и возможная замена."),
                Reminder("Низкий уровень масла", "2025-03-10", "2025-04-01", "Масло почти на минимуме – проверить утечки и долить масло."),
                Reminder("Давление передней правой шины", "2025-03-10", "2025-04-01", "Давление в шине ниже нормы, необходимо проверить и подкачать шину.")
            )
        )
    }

    val maintenanceLogsState = remember {
        mutableStateOf(
            listOf(
                MaintenanceLog("Обслуживание коробки передач", "Замена", "2025-03-05", "10000"),
                MaintenanceLog("Обслуживание дисков", "Покупка", "2025-03-06", "8000"),
                MaintenanceLog("Обслуживание датчиков", "Замена", "2025-03-07", "3000")
            )
        )
    }

    val surveyDataState = remember { mutableStateOf<SurveyData?>(null) }


}
