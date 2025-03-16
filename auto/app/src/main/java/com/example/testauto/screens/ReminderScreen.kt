package com.example.testauto.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.testauto.R
import com.example.testauto.components.CustomTopBar
import com.example.testauto.components.DateInputField
import com.example.testauto.components.InfoDialog
import com.example.testauto.models.Reminder
import com.example.testauto.ui.theme.getColorFromResources
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun ReminderScreen(
    navController: NavHostController,
    remindersState: MutableState<List<Reminder>>
) {
    var showAddCard by remember { mutableStateOf(false) }
    var showDetailDialog by remember { mutableStateOf(false) }
    var selectedReminder by remember { mutableStateOf<Reminder?>(null) }
    var showInfo by remember { mutableStateOf(false) }
    if (showInfo) {
        InfoDialog { showInfo = false }
    }

    // Промежуточные переменные для фильтров
    var tempFilterStartDate by remember { mutableStateOf("") }
    var tempFilterEndDate by remember { mutableStateOf("") }
    var tempFilterMinMileage by remember { mutableStateOf("") }
    var tempFilterMaxMileage by remember { mutableStateOf("") }

    // Финальные переменные для фильтров
    var filterStartDate by remember { mutableStateOf("") }
    var filterEndDate by remember { mutableStateOf("") }
    var filterMinMileage by remember { mutableStateOf("") }
    var filterMaxMileage by remember { mutableStateOf("") }

    var showFilterDialog by remember { mutableStateOf(false) }

    var searchQuery by remember { mutableStateOf("") }
    var isDateAscending by remember { mutableStateOf(true) }
    var isMileageAscending by remember { mutableStateOf(true) }

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    val filteredReminders = remindersState.value.filter { reminder ->
        val repairDate = LocalDate.parse(reminder.repairDate, formatter)
        val startDate = if (filterStartDate.isNotEmpty()) LocalDate.parse(filterStartDate, formatter) else LocalDate.MIN
        val endDate = if (filterEndDate.isNotEmpty()) LocalDate.parse(filterEndDate, formatter) else LocalDate.MAX
        val minMileage = filterMinMileage.toIntOrNull() ?: Int.MIN_VALUE
        val maxMileage = filterMaxMileage.toIntOrNull() ?: Int.MAX_VALUE

        (searchQuery.isEmpty() || reminder.title.contains(searchQuery, ignoreCase = true)) &&
                (repairDate >= startDate && repairDate <= endDate) &&
                (reminder.mileage >= minMileage && reminder.mileage <= maxMileage)
    }

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    // Сортировка по дате при первом рендере
    LaunchedEffect(Unit) {
        remindersState.value = remindersState.value.sortedBy {
            LocalDate.parse(it.repairDate, formatter)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomTopBar(
                title = "Напоминания",
                onInfoClick = { showInfo = true },
                onProfileClick = { navController.navigate("survey") },
                showBackButton = true,
                onBack = { navController.navigate("home") }
            )
        },
        floatingActionButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = 32.dp) // Дополнительный отступ слева
                        .background(
                            color = Color.Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable {
                            filterStartDate = ""
                            filterEndDate = ""
                            filterMinMileage = ""
                            filterMaxMileage = ""
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = "Сбросить", color = Color.White)
                }
                Box(
                    modifier = Modifier
                        .background(
                            color = getColorFromResources(R.color.main_color),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable { showFilterDialog = true }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Фильтр", color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = "Filter",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
    }

}