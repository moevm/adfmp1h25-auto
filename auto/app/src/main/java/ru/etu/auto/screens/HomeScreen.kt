package ru.etu.auto.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.etu.auto.components.CustomTopBar
import ru.etu.auto.components.InfoDialog
import ru.etu.auto.data.CarData
import ru.etu.auto.models.MaintenanceLog
import ru.etu.auto.models.Reminder
import ru.etu.auto.models.SurveyData
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun HomeScreen(
    navController: NavHostController,
    remindersState: MutableState<List<Reminder>>,
    maintenanceLogs: List<MaintenanceLog>,
    surveyDataState: MutableState<SurveyData?>
) {
    var showInfo by remember { mutableStateOf(false) }
    if (showInfo) {
        InfoDialog { showInfo = false }
    }

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val reminders = remindersState.value
    val nearestReminder = reminders.minByOrNull { LocalDate.parse(it.repairDate, formatter) }
    val lastCheck = maintenanceLogs.maxByOrNull { LocalDate.parse(it.date, formatter) }

    // Автоматическая генерация напоминаний
    LaunchedEffect(surveyDataState.value) {
        val surveyData = surveyDataState.value ?: return@LaunchedEffect
        val currentMileage = surveyData.mileage.toIntOrNull() ?: 0
        val monthlyMileage = surveyData.avgKm.toIntOrNull() ?: 0 // Месячная норма из профиля
        val nextMonthMileage = currentMileage + monthlyMileage // Пробег через месяц
        val recommendation = CarData.getRecommendations(surveyData.brand, surveyData.model)

        val newReminders = mutableListOf<Reminder>()
        val existingTitles = reminders.map { it.title }
        val currentDate = LocalDate.now().format(formatter) // Текущая дата для dateAdded

        listOf(
            "Замена масла" to recommendation.oilChangeIntervalKm,
            "Полная проверка" to recommendation.fullInspectionIntervalKm,
            "Ротация шин" to recommendation.tireRotationIntervalKm,
            "Проверка тормозов" to recommendation.brakeCheckIntervalKm
        ).forEach { (title, interval) ->
            // Вычисляем ближайший следующий интервал обслуживания
            val nextServiceMileage = ((currentMileage / interval) + 1) * interval
            // Проверяем, попадает ли он в диапазон следующего месяца
            if (nextServiceMileage in (currentMileage + 1)..nextMonthMileage && title !in existingTitles) {
                val dueDate = LocalDate.now().plusDays(30).format(formatter) // Срок — 30 дней
                newReminders.add(
                    Reminder(
                        title = title,
                        dateAdded = currentDate,
                        repairDate = dueDate,
                        description = "Пора провести $title (пробег достигнет $nextServiceMileage км)",
                        mileage = currentMileage
                    )
                )
            }
        }

        if (newReminders.isNotEmpty()) {
            remindersState.value = reminders + newReminders
        }
    }

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

            Card(
                modifier = cardModifier,
                backgroundColor = Color.LightGray,
                shape = RoundedCornerShape(8.dp),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Всего напоминаний: ${reminders.size}", style = MaterialTheme.typography.h6)
                    Divider(color = Color.Gray, thickness = 1.dp)
                    nearestReminder?.let {
                        val daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(it.repairDate, formatter))
                        if (daysLeft >= 0) {
                            Text(
                                "Ближайшее напоминание: \"${it.title}\" через $daysLeft дней",
                                color = Color.Red,
                                style = MaterialTheme.typography.body1
                            )
                        } else {
                            Text(
                                "Срок напоминания \"${it.title}\" истек ${-daysLeft} дней назад",
                                color = Color.Red,
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                    Divider(color = Color.Gray, thickness = 1.dp)
                    lastCheck?.let {
                        Text("Последняя проверка: ${it.name}, ${it.workType} (Дата: ${it.date})")
                    }
                    surveyDataState.value?.let {
                        Text("Текущий пробег: ${it.mileage} км")
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
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