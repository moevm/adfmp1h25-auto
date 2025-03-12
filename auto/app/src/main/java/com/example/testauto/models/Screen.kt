package com.example.testauto.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object Home : Screen("home", Icons.Default.Home, "Главная")
    object Reminders : Screen("reminders", Icons.Default.Notifications, "Напоминания")
    object Maintenance : Screen("maintenance", Icons.Default.Build, "Обслуживания")
}
