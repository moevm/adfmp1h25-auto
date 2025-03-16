package com.example.testauto.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.testauto.R
import com.example.testauto.components.CustomTopBar
import com.example.testauto.components.InfoDialog
import com.example.testauto.models.SurveyData
import com.example.testauto.ui.theme.getColorFromResources

@Composable
fun SurveyScreen(
    navController: NavHostController,
    surveyDataState: MutableState<SurveyData?>
) {
    var showInfo by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("SurveyPrefs", Context.MODE_PRIVATE)

    // Загрузка данных из SharedPreferences с значениями по умолчанию
    var name by remember {
        mutableStateOf(sharedPreferences.getString("name", "") ?: "")
    }
    var selectedKm by remember {
        mutableStateOf(sharedPreferences.getString("avgKm", "0") ?: "0")
    }
    var selectedBrand by remember {
        mutableStateOf(sharedPreferences.getString("brand", "Lada") ?: "Lada")
    }
    var selectedModel by remember {
        mutableStateOf(sharedPreferences.getString("model", "Vesta") ?: "Vesta")
    }
    var year by remember {
        mutableStateOf(sharedPreferences.getString("year", "2025") ?: "2025")
    }
    var mileage by remember {
        mutableStateOf(sharedPreferences.getString("mileage", "0") ?: "0")
    }

    // Варианты для выпадающих списков
    val brandModels = mapOf(
        "Lada" to listOf("Vesta", "Granta", "Niva"),
        "Audi" to listOf("A3", "A4", "Q5"),
        "Toyota" to listOf("Corolla", "Camry", "RAV4"),
        "Volkswagen" to listOf("Golf", "Passat", "Tiguan"),
        "Ford" to listOf("Focus", "Fiesta", "Explorer"),
        "Honda" to listOf("Civic", "Accord", "CR-V"),
        "Chevrolet" to listOf("Cruze", "Malibu", "Impala"),
        "Mercedes-Benz" to listOf("C-Class", "E-Class", "S-Class"),
        "BMW" to listOf("3 Series", "5 Series", "X3"),
        "Nissan" to listOf("Sentra", "Altima", "Rogue"),
        "Hyundai" to listOf("Elantra", "Sonata", "Tucson"),
        "Kia" to listOf("Rio", "Optima", "Sportage"),
        "Subaru" to listOf("Impreza", "Forester", "Outback")
    )
    val brands = brandModels.keys.toList().sorted()
    val years = (1990..2025).map { it.toString() }.reversed() // Список годов от 2025 до 1990

    // Состояния для выпадающих меню
    var brandDropdownExpanded by remember { mutableStateOf(false) }
    var modelDropdownExpanded by remember { mutableStateOf(false) }
    var yearDropdownExpanded by remember { mutableStateOf(false) }

    // Диалог информации
    if (showInfo) {
        InfoDialog { showInfo = false }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "",
                showBackButton = true,
                onBack = {
                    navController.navigate("home") {
                        popUpTo("home") {
                            inclusive = true
                        }
                    }
                },
                onInfoClick = { showInfo = true },
                onProfileClick = { }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Добавляем прокрутку
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
// 1. Поле для ввода имени
            Text("Имя")
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Введите имя") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words, // Первая буква каждого слова заглавная
                    keyboardType = KeyboardType.Text // Оставляем текстовый тип ввода
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = getColorFromResources(R.color.main_color),
                    unfocusedBorderColor = getColorFromResources(R.color.main_color),
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    placeholderColor = Color.Black
                )
            )

            // 2. Поле для ввода среднего пробега в месяц с шагом +5
            Text("Сколько в среднем км проезжает авто в месяц?")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val currentKm = selectedKm.toIntOrNull() ?: 0
                        if (currentKm >= 5) selectedKm = (currentKm - 5).toString()
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Text("-")
                }
                OutlinedTextField(
                    value = selectedKm,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                            selectedKm = newValue
                        }
                    },
                    label = { Text("Введите км") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = getColorFromResources(R.color.main_color),
                        unfocusedBorderColor = getColorFromResources(R.color.main_color),
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        placeholderColor = Color.Black
                    )
                )
                Button(
                    onClick = {
                        val currentKm = selectedKm.toIntOrNull() ?: 0
                        selectedKm = (currentKm + 5).toString()
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Text("+")
                }
            }

// 3. Поле для выбора марки
            Text("Марка автомобиля")
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = selectedBrand,
                    onValueChange = { selectedBrand = it }, // Разрешаем ручной ввод
                    label = { Text("Введите или выберите марку") },
                    modifier = Modifier.weight(1f), // Поле занимает доступное пространство
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = getColorFromResources(R.color.main_color),
                        unfocusedBorderColor = getColorFromResources(R.color.main_color),
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        placeholderColor = Color.Black
                    )
                )
                Box {
                    IconButton(onClick = { brandDropdownExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown, // Иконка стрелки вниз
                            contentDescription = "Открыть список марок",
                            tint = getColorFromResources(R.color.main_color)
                        )
                        DropdownMenu(
                            expanded = brandDropdownExpanded,
                            onDismissRequest = { brandDropdownExpanded = false },
                            modifier = Modifier
                                .width(IntrinsicSize.Min) // Ширина подстраивается под содержимое
                                .heightIn(max = 200.dp) // Ограничение высоты
                        ) {
                            val hardcodedBrands = listOf("Lada", "Toyota", "Audi", "BMW", "Ford")
                            hardcodedBrands.forEach { brand ->
                                DropdownMenuItem(onClick = {
                                    selectedBrand = brand
                                    selectedModel = brandModels[brand]?.first() ?: ""
                                    brandDropdownExpanded = false
                                }) {
                                    Text(brand)
                                }
                            }
                        }
                    }
                }
            }

            // 4. Поле для выбора модели
            Text("Модель автомобиля")
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = selectedModel,
                    onValueChange = { selectedModel = it }, // Разрешаем ручной ввод
                    label = { Text("Введите или выберите модель") },
                    modifier = Modifier.weight(1f), // Поле занимает доступное пространство
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Ascii, // Ограничиваем ввод ASCII-символами (английская раскладка)
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = getColorFromResources(R.color.main_color),
                        unfocusedBorderColor = getColorFromResources(R.color.main_color),
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        placeholderColor = Color.Black
                    )
                )
                Box {
                    IconButton(onClick = { modelDropdownExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown, // Иконка стрелки вниз
                            contentDescription = "Открыть список моделей",
                            tint = getColorFromResources(R.color.main_color)
                        )

                        DropdownMenu(
                            expanded = modelDropdownExpanded,
                            onDismissRequest = { modelDropdownExpanded = false },
                            modifier = Modifier
                                .width(IntrinsicSize.Min) // Ширина подстраивается под содержимое
                                .heightIn(max = 200.dp) // Ограничение высоты
                        ) {
                            // Захардкоженные модели для каждой марки
                            val hardcodedModels = when (selectedBrand) {
                                "Lada" -> listOf("Vesta", "Granta", "Niva", "Kalina", "Priora")
                                "Toyota" -> listOf(
                                    "Corolla",
                                    "Camry",
                                    "RAV4",
                                    "Prius",
                                    "Land Cruiser"
                                )

                                "Audi" -> listOf("A3", "A4", "Q5", "A6", "Q7")
                                "BMW" -> listOf("3 Series", "5 Series", "X3", "X5", "7 Series")
                                "Ford" -> listOf("Focus", "Fiesta", "Explorer", "Mustang", "F-150")
                                else -> listOf() // Пустой список для пользовательских марок
                            }
                            hardcodedModels.forEach { model ->
                                DropdownMenuItem(onClick = {
                                    selectedModel = model
                                    modelDropdownExpanded = false
                                }) {
                                    Text(model)
                                }
                            }
                        }
                    }
                }
            }

            // 5. Поле для выбора года выпуска
            Text("Год выпуска")
            Box {
                OutlinedTextField(
                    value = year,
                    onValueChange = {}, // Пустая функция, так как поле только для чтения
                    readOnly = true,
                    label = { Text("Выберите год") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = getColorFromResources(R.color.main_color),
                        unfocusedBorderColor = getColorFromResources(R.color.main_color),
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        placeholderColor = Color.Black
                    ),
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect { interaction ->
                                    if (interaction is PressInteraction.Release) {
                                        yearDropdownExpanded = true
                                    }
                                }
                            }
                        }
                )
                DropdownMenu(
                    expanded = yearDropdownExpanded,
                    onDismissRequest = { yearDropdownExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp) // Ограничение максимальной высоты до 200dp
                ) {
                    years.forEach { yearOption ->
                        DropdownMenuItem(onClick = {
                            year = yearOption
                            yearDropdownExpanded = false
                        }) {
                            Text(yearOption)
                        }
                    }
                }
            }

            // 6. Поле для текущего пробега с шагом +1000
            Text("Текущий пробег")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val currentMileage = mileage.toIntOrNull() ?: 0
                        if (currentMileage >= 1000) mileage = (currentMileage - 1000).toString()
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Text("-")
                }
                OutlinedTextField(
                    value = mileage,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                            mileage = newValue
                        }
                    },
                    label = { Text("Введите пробег (км)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = getColorFromResources(R.color.main_color),
                        unfocusedBorderColor = getColorFromResources(R.color.main_color),
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        placeholderColor = Color.Black
                    )
                )
                Button(
                    onClick = {
                        val currentMileage = mileage.toIntOrNull() ?: 0
                        mileage = (currentMileage + 1000).toString()
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Text("+")
                }
            }

            // Уведомительный текст перед кнопками
            Text(
                text = "Внимание - При выборе своих вариантов, рекомендации будут общего характера",
                style = MaterialTheme.typography.body2, // Мелкий текст для уведомления
                color = Color.Yellow, // Серый цвет для ненавязчивости
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

// 7. Кнопки "Закрыть" и "Сохранить"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        navController.popBackStack() // Возврат на предыдущий экран
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                ) {
                    Text("Закрыть", color = Color.White)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        // Проверка на заполненность полей
                        if (name.isNotEmpty() && selectedKm.isNotEmpty() && selectedBrand.isNotEmpty() &&
                            selectedModel.isNotEmpty() && year.isNotEmpty() && mileage.isNotEmpty()
                        ) {
                            // Сохранение данных в SharedPreferences
                            val sharedPreferences = context.getSharedPreferences("SurveyPrefs", Context.MODE_PRIVATE)
                            with(sharedPreferences.edit()) {
                                putString("name", name)
                                putString("avgKm", selectedKm)
                                putString("brand", selectedBrand)
                                putString("model", selectedModel)
                                putString("year", year)
                                putString("mileage", mileage)
                                apply() // Асинхронное сохранение
                            }
                            // Обновление состояния
                            surveyDataState.value = SurveyData(
                                name,
                                selectedKm,
                                selectedBrand,
                                selectedModel,
                                year,
                                mileage
                            )
                            Toast.makeText(context, "Данные сохранены", Toast.LENGTH_SHORT).show()
                            navController.popBackStack() // Возврат на предыдущий экран после сохранения
                        } else {
                            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Сохранить", color = Color.White)
                }
            }
        }
    }
}