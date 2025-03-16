package com.example.testauto.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.testauto.R
import com.example.testauto.components.CustomTopBar
import com.example.testauto.components.DateInputField
import com.example.testauto.components.InfoDialog
import com.example.testauto.models.MaintenanceLog
import com.example.testauto.ui.theme.getColorFromResources
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

@Composable
fun MaintenanceScreen(
    navController: NavHostController,
    logsState: MutableState<List<MaintenanceLog>>
) {
    var showAddCard by remember { mutableStateOf(false) } // Заменили showAddDialog на showAddCard
    var showInfo by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedLog by remember { mutableStateOf<MaintenanceLog?>(null) }
    var showDetailDialog by remember { mutableStateOf(false) }

    // Промежуточные переменные для фильтров
    var tempFilterWorkType by remember { mutableStateOf("") }
    var tempFilterStartDate by remember { mutableStateOf("") }
    var tempFilterEndDate by remember { mutableStateOf("") }
    var tempFilterCost by remember { mutableStateOf(0f) }

    // Финальные переменные для фильтров
    var filterWorkType by remember { mutableStateOf("") }
    var filterStartDate by remember { mutableStateOf("") }
    var filterEndDate by remember { mutableStateOf("") }
    var filterCost by remember { mutableStateOf(0f) }

    val costRange = 0f..20000f

    var searchQuery by remember { mutableStateOf("") }
    var isDateAscending by remember { mutableStateOf(true) }
    var isCostAscending by remember { mutableStateOf(true) }

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    // Фильтрация
    val filteredLogs = logsState.value.filter { log ->
        val logDate = try { LocalDate.parse(log.date, formatter) } catch (e: Exception) { LocalDate.MIN }
        val startDate = if (filterStartDate.isNotEmpty()) LocalDate.parse(filterStartDate, formatter) else LocalDate.MIN
        val endDate = if (filterEndDate.isNotEmpty()) LocalDate.parse(filterEndDate, formatter) else LocalDate.MAX
        val logCost = log.cost.filter { it.isDigit() || it == '.' }.toFloatOrNull() ?: 0f

        (searchQuery.isEmpty() || log.name.contains(searchQuery, ignoreCase = true) || log.workType.contains(searchQuery, ignoreCase = true)) &&
                (filterWorkType.isEmpty() || log.workType.contains(filterWorkType, ignoreCase = true)) &&
                (logDate >= startDate && logDate <= endDate) &&
                (filterCost == 0f || logCost <= filterCost)
    }

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    if (showInfo) {
        InfoDialog { showInfo = false }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Обслуживания",
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
                        .padding(start = 32.dp)
                        .background(Color(0xFF757575), RoundedCornerShape(4.dp))
                        .clickable {
                            filterWorkType = ""
                            filterStartDate = ""
                            filterEndDate = ""
                            filterCost = 0f
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = "Сбросить", color = Color.White)
                }
                Box(
                    modifier = Modifier
                        .background(getColorFromResources(R.color.main_color), RoundedCornerShape(4.dp))
                        .clickable { showFilterDialog = true }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn {
                items(filteredLogs) { log ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                selectedLog = log
                                showDetailDialog = true
                            },
                        elevation = 4.dp,
                        backgroundColor = Color(0xFFF5F5F5)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = log.name,
                                style = MaterialTheme.typography.subtitle1,
                                color = Color.Black
                            )
                            Text(
                                text = "Тип работ: ${log.workType}",
                                style = MaterialTheme.typography.body2,
                                color = Color.DarkGray
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Дата: ${log.date}",
                                    style = MaterialTheme.typography.body2,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = "Стоимость: ${log.cost} руб",
                                    style = MaterialTheme.typography.body2,
                                    color = Color(0xFF4CAF50)
                                )
                            }
                        }
                    }
                }
                if (showAddCard) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = 4.dp
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                var newName by remember { mutableStateOf("") }
                                var newWorkType by remember { mutableStateOf("") }
                                var newDate by remember { mutableStateOf("") }
                                var newCost by remember { mutableStateOf("") }

                                OutlinedTextField(
                                    value = newName,
                                    onValueChange = { newName = it },
                                    label = { Text("Название") },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        focusedBorderColor = Color(0xFF4CAF50),
                                        unfocusedBorderColor = Color.Gray,
                                        focusedLabelColor = Color(0xFF4CAF50),
                                        unfocusedLabelColor = Color.Gray
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = newWorkType,
                                    onValueChange = { newWorkType = it },
                                    label = { Text("Тип работ") },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        focusedBorderColor = Color(0xFF4CAF50),
                                        unfocusedBorderColor = Color.Gray,
                                        focusedLabelColor = Color(0xFF4CAF50),
                                        unfocusedLabelColor = Color.Gray
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                DateInputField(
                                    label = "Дата",
                                    date = newDate,
                                    onDateSelected = { newDate = it },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = newCost,
                                    onValueChange = { if (it.all { it.isDigit() || it == '.' } || it.isEmpty()) newCost = it },
                                    label = { Text("Стоимость (руб)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        focusedBorderColor = Color(0xFF4CAF50),
                                        unfocusedBorderColor = Color.Gray,
                                        focusedLabelColor = Color(0xFF4CAF50),
                                        unfocusedLabelColor = Color.Gray
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Button(
                                        onClick = {
                                            if (newName.isNotEmpty() && newWorkType.isNotEmpty() && newDate.isNotEmpty() && newCost.isNotEmpty()) {
                                                logsState.value = logsState.value + MaintenanceLog(newName, newWorkType, newDate, newCost)
                                                showAddCard = false
                                            } else {
                                                scope.launch {
                                                    scaffoldState.snackbarHostState.showSnackbar(
                                                        "Заполните все поля"
                                                    )
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50))
                                    ) {
                                        Text("Добавить", color = Color.White)
                                    }
                                    Button(
                                        onClick = { showAddCard = false },
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
                                    ) {
                                        Text("Закрыть", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp)
                    .clickable { showAddCard = true }, // Изменили на showAddCard
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Добавить",
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.padding(end = 4.dp)
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color(0xFF4CAF50).copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }


}