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


}
