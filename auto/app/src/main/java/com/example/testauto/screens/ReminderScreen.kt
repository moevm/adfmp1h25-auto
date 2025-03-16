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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Сортировка", style = MaterialTheme.typography.body1)
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(
                        modifier = Modifier
                            .background(
                                color = getColorFromResources(R.color.main_color),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable {
                                isDateAscending = !isDateAscending
                                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                                remindersState.value = if (isDateAscending) {
                                    remindersState.value.sortedBy {
                                        LocalDate.parse(it.repairDate, formatter)
                                    }
                                } else {
                                    remindersState.value.sortedByDescending {
                                        LocalDate.parse(it.repairDate, formatter)
                                    }
                                }
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Дата", color = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (isDateAscending) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Sort Date",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(
                        modifier = Modifier
                            .background(
                                color = getColorFromResources(R.color.main_color),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable {
                                isMileageAscending = !isMileageAscending
                                remindersState.value = if (isMileageAscending) {
                                    remindersState.value.sortedBy { it.mileage }
                                } else {
                                    remindersState.value.sortedByDescending { it.mileage }
                                }
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Пробег", color = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (isMileageAscending) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Sort Mileage",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(text = "Поиск", color = getColorFromResources(R.color.main_color)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = getColorFromResources(R.color.main_color),
                        unfocusedBorderColor = getColorFromResources(R.color.main_color)
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn {
                if (showAddCard) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = 4.dp
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                var title by remember { mutableStateOf("") }
                                var repairDate by remember { mutableStateOf("") }
                                var mileage by remember { mutableStateOf("") }
                                var description by remember { mutableStateOf("") }

                                OutlinedTextField(
                                    value = title,
                                    onValueChange = { title = it },
                                    label = { Text("Заголовок") },
                                    keyboardOptions = KeyboardOptions(
                                        capitalization = KeyboardCapitalization.Words,
                                        keyboardType = KeyboardType.Text
                                    ),
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        focusedBorderColor = Color(0xFF6495ED).copy(alpha = 0.7f),
                                        unfocusedBorderColor = Color(0xFF6495ED).copy(alpha = 0.7f),
                                        disabledBorderColor = Color(0xFF6495ED).copy(alpha = 0.7f),
                                        errorBorderColor = Color(0xFF6495ED).copy(alpha = 0.7f),
                                        focusedLabelColor = Color(0xFF6495ED).copy(alpha = 0.7f),
                                        unfocusedLabelColor = Color(0xFF6495ED).copy(alpha = 0.7f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                DateInputField(
                                    label = "Дата",
                                    date = repairDate,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    onDateSelected = { repairDate = it }
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = mileage,
                                    onValueChange = { newValue ->
                                        if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                                            mileage = newValue
                                        }
                                    },
                                    label = { Text("Пробег (км), опционально") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    ),
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        focusedBorderColor = Color(0xFF6495ED).copy(alpha = 0.7f),
                                        unfocusedBorderColor = Color(0xFF6495ED).copy(alpha = 0.7f),
                                        disabledBorderColor = Color(0xFF6495ED).copy(alpha = 0.7f),
                                        errorBorderColor = Color(0xFF6495ED).copy(alpha = 0.7f),
                                        focusedLabelColor = Color(0xFF6495ED).copy(alpha = 0.7f),
                                        unfocusedLabelColor = Color(0xFF6495ED).copy(alpha = 0.7f)
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = description,
                                    onValueChange = { description = it },
                                    label = { Text("Описание проблемы, опционально") },
                                    keyboardOptions = KeyboardOptions(
                                        capitalization = KeyboardCapitalization.Words,
                                        keyboardType = KeyboardType.Text
                                    ),
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        focusedBorderColor = Color(0xFF6495ED).copy(alpha = 0.7f),
                                        unfocusedBorderColor = Color(0xFF6495ED).copy(alpha = 0.7f),
                                        disabledBorderColor = Color(0xFF6495ED).copy(alpha = 0.7f),
                                        errorBorderColor = Color(0xFF6495ED).copy(alpha = 0.7f),
                                        focusedLabelColor = Color(0xFF6495ED).copy(alpha = 0.7f),
                                        unfocusedLabelColor = Color(0xFF6495ED).copy(alpha = 0.7f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
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
                                            if (title.isNotEmpty() && repairDate.isNotEmpty()) {
                                                remindersState.value = remindersState.value + Reminder(
                                                    title = title,
                                                    dateAdded = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                                                    repairDate = repairDate,
                                                    description = description,
                                                    mileage = mileage.toIntOrNull() ?: 0
                                                )
                                                showAddCard = false
                                            } else {
                                                scope.launch {
                                                    scaffoldState.snackbarHostState.showSnackbar(
                                                        "Заполните необходимые поля: Заголовок и Дата"
                                                    )
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = getColorFromResources(R.color.green)
                                        )
                                    ) {
                                        Text(text = "Добавить", color = Color.White)
                                    }
                                    Button(
                                        onClick = { showAddCard = false },
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color.Gray
                                        )
                                    ) {
                                        Text(text = "Закрыть", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
                items(filteredReminders) { reminder ->
                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    val currentDate = LocalDate.now()
                    val repairDate = LocalDate.parse(reminder.repairDate, formatter)
                    val daysUntilRepair = ChronoUnit.DAYS.between(currentDate, repairDate).toInt()

                    Log.d("ReminderScreen", "Days until repair for ${reminder.title}: $daysUntilRepair")
                    val cardColor = when {
                        daysUntilRepair < 0 -> Color(0xFFFF2400)
                        daysUntilRepair <= 1 -> Color.Red.copy(alpha = 0.5f)
                        daysUntilRepair <= 7 -> Color.Yellow.copy(alpha = 0.8f)
                        daysUntilRepair <= 30 -> getColorFromResources(R.color.main_color).copy(alpha = 0.5f)
                        else -> Color.Gray.copy(alpha = 0.5f)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                selectedReminder = reminder
                                showDetailDialog = true
                            },
                        elevation = 4.dp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(cardColor)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = reminder.title,
                                    style = MaterialTheme.typography.subtitle1
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "На пробеге: ${reminder.mileage} км",
                                        style = MaterialTheme.typography.body2
                                    )
                                    Text(
                                        text = reminder.repairDate,
                                        style = MaterialTheme.typography.body2
                                    )
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
                    .clickable { showAddCard = true },
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Добавить",
                    color = getColorFromResources(R.color.green),
                    modifier = Modifier.padding(end = 4.dp)
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color.Green.copy(alpha = 0.1f), shape = RoundedCornerShape(4.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.Green,
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }

    if (showDetailDialog && selectedReminder != null) {
        AlertDialog(
            onDismissRequest = { showDetailDialog = false },
            title = { Text(selectedReminder!!.title) },
            text = {
                Column {
                    Text(
                        text = if (selectedReminder!!.description.isNotEmpty()) {
                            selectedReminder!!.description
                        } else {
                            "Нет подробностей"
                        },
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (selectedReminder!!.mileage > 0) {
                                "На пробеге: ${selectedReminder!!.mileage} км"
                            } else {
                                "Пробег не указан"
                            },
                            style = MaterialTheme.typography.body2
                        )
                        Text(
                            text = selectedReminder!!.repairDate,
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = {
                            remindersState.value = remindersState.value.filter { it != selectedReminder }
                            showDetailDialog = false
                        }
                    ) {
                        Text("Удалить", color = Color.Red)
                    }
                    TextButton(onClick = { showDetailDialog = false }) {
                        Text("Закрыть", color = Color.Gray)
                    }
                }
            }
        )
    }
}