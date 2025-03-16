package com.example.testauto.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.testauto.components.CustomTopBar
import com.example.testauto.components.InfoDialog
import com.example.testauto.models.MaintenanceLog
import com.example.testauto.models.Reminder
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun HomeScreen(
    navController: NavHostController,
    reminders: List<Reminder>,
    maintenanceLogs: List<MaintenanceLog>
) {
    var showInfo by remember { mutableStateOf(false) }
    if (showInfo) {
        InfoDialog { showInfo = false }
    }

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val nearestReminder = reminders.minByOrNull { LocalDate.parse(it.repairDate, formatter) }
    val lastCheck = maintenanceLogs.maxByOrNull { LocalDate.parse(it.date, formatter) }


    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Главная",
                onInfoClick = { showInfo = true },
                onProfileClick = { navController.navigate("survey") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val cardModifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

            Button(
                onClick = {
                    navController.navigate("reminders") {
                        popUpTo("home") { saveState = true; inclusive = false }
                        launchSingleTop = true
                    }
                },
                modifier = cardModifier.height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Добавить напоминание")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    navController.navigate("maintenance") {
                        popUpTo("home") { saveState = true; inclusive = false }
                        launchSingleTop = true
                    }
                },
                modifier = cardModifier.height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Добавить обслуживание")
            }
        }
    }
}
