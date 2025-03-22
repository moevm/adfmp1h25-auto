package ru.etu.auto.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.etu.auto.components.CustomTopBar
import ru.etu.auto.components.InfoDialog
import ru.etu.auto.data.CarData
import ru.etu.auto.models.SurveyData
import ru.etu.auto.R
import ru.etu.auto.shared.getColorFromResources

@Composable
fun SurveyScreen(
    navController: NavHostController,
    surveyDataState: MutableState<SurveyData?>
) {
    var showInfo by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("SurveyPrefs", Context.MODE_PRIVATE)

    var name by remember { mutableStateOf(sharedPreferences.getString("name", "") ?: "") }
    var selectedKm by remember { mutableStateOf(sharedPreferences.getString("avgKm", "0") ?: "0") }
    var selectedBrand by remember { mutableStateOf(sharedPreferences.getString("brand", "Lada") ?: "Lada") }
    var selectedModel by remember { mutableStateOf(sharedPreferences.getString("model", "Vesta") ?: "Vesta") }
    var year by remember { mutableStateOf(sharedPreferences.getString("year", "2025") ?: "2025") }
    var mileage by remember { mutableStateOf(sharedPreferences.getString("mileage", "0") ?: "0") }

    // Состояния ошибок для каждого поля
    var nameError by remember { mutableStateOf(false) }
    var kmError by remember { mutableStateOf(false) }
    var brandError by remember { mutableStateOf(false) }
    var modelError by remember { mutableStateOf(false) }
    var yearError by remember { mutableStateOf(false) }
    var mileageError by remember { mutableStateOf(false) }

    val brands = CarData.getBrands()
    val models = CarData.getModelsForBrand(selectedBrand)
    val years = (1990..2025).map { it.toString() }.reversed()

    var brandDropdownExpanded by remember { mutableStateOf(false) }
    var modelDropdownExpanded by remember { mutableStateOf(false) }
    var yearDropdownExpanded by remember { mutableStateOf(false) }
    var brandSearchQuery by remember { mutableStateOf("") }
    var modelSearchQuery by remember { mutableStateOf("") }

    if (showInfo) {
        InfoDialog { showInfo = false }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "",
                showBackButton = true,
                onBack = { navController.navigate("home") { popUpTo("home") { inclusive = true } } },
                onInfoClick = { showInfo = true },
                onProfileClick = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Имя
            Text("Имя")
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; nameError = false },
                label = { Text("Введите имя") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = getColorFromResources(R.color.main_color),
                    unfocusedBorderColor = getColorFromResources(R.color.main_color),
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    placeholderColor = Color.Black
                )
            )
            if (nameError) {
                Text("Заполните поле", color = Color.Red, style = MaterialTheme.typography.caption)
            }

            // Средний пробег
            Text("Сколько в среднем км проезжает авто в месяц?")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    val currentKm = selectedKm.toIntOrNull() ?: 0
                    if (currentKm >= 5) selectedKm = (currentKm - 5).toString()
                    kmError = false
                }, modifier = Modifier.size(48.dp)) { Text("-") }
                OutlinedTextField(
                    value = selectedKm,
                    onValueChange = { if (it.all { char -> char.isDigit() } || it.isEmpty()) { selectedKm = it; kmError = false } },
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
                Button(onClick = {
                    val currentKm = selectedKm.toIntOrNull() ?: 0
                    selectedKm = (currentKm + 5).toString()
                    kmError = false
                }, modifier = Modifier.size(48.dp)) { Text("+") }
            }
            if (kmError) {
                Text("Заполните поле", color = Color.Red, style = MaterialTheme.typography.caption, modifier = Modifier.padding(start = 56.dp))
            }

            // Марка автомобиля
            Text("Марка автомобиля")
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = selectedBrand,
                    onValueChange = { selectedBrand = it; brandError = false },
                    label = { Text("Введите или выберите марку") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, keyboardType = KeyboardType.Text),
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
                        Icon(Icons.Default.ArrowDropDown, "Открыть список марок", tint = getColorFromResources(R.color.main_color))
                    }
                    DropdownMenu(
                        expanded = brandDropdownExpanded,
                        onDismissRequest = { brandDropdownExpanded = false },
                        modifier = Modifier.heightIn(max = 200.dp)
                    ) {
                        if (brands.size > 10) {
                            OutlinedTextField(
                                value = brandSearchQuery,
                                onValueChange = { brandSearchQuery = it },
                                label = { Text("Поиск марки") },
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = getColorFromResources(R.color.main_color),
                                    unfocusedBorderColor = getColorFromResources(R.color.main_color),
                                    focusedLabelColor = Color.Black,
                                    unfocusedLabelColor = Color.Black,
                                    placeholderColor = Color.Black
                                )
                            )
                        }
                        val filteredBrands = brands.filter { it.contains(brandSearchQuery, ignoreCase = true) }
                        filteredBrands.forEach { brand ->
                            DropdownMenuItem(onClick = {
                                selectedBrand = brand
                                selectedModel = CarData.getModelsForBrand(brand).first()
                                brandSearchQuery = ""
                                brandDropdownExpanded = false
                                brandError = false
                            }) {
                                Text(brand)
                            }
                        }
                    }
                }
            }
            if (brandError) {
                Text("Заполните поле", color = Color.Red, style = MaterialTheme.typography.caption)
            }

            // Модель автомобиля
            Text("Модель автомобиля")
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = selectedModel,
                    onValueChange = { selectedModel = it; modelError = false },
                    label = { Text("Введите или выберите модель") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, keyboardType = KeyboardType.Ascii),
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
                        Icon(Icons.Default.ArrowDropDown, "Открыть список моделей", tint = getColorFromResources(R.color.main_color))
                    }
                    DropdownMenu(
                        expanded = modelDropdownExpanded,
                        onDismissRequest = { modelDropdownExpanded = false },
                        modifier = Modifier.heightIn(max = 200.dp)
                    ) {
                        if (models.size > 10) {
                            OutlinedTextField(
                                value = modelSearchQuery,
                                onValueChange = { modelSearchQuery = it },
                                label = { Text("Поиск модели") },
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = getColorFromResources(R.color.main_color),
                                    unfocusedBorderColor = getColorFromResources(R.color.main_color),
                                    focusedLabelColor = Color.Black,
                                    unfocusedLabelColor = Color.Black,
                                    placeholderColor = Color.Black
                                )
                            )
                        }
                        val filteredModels = models.filter { it.contains(modelSearchQuery, ignoreCase = true) }
                        filteredModels.forEach { model ->
                            DropdownMenuItem(onClick = {
                                selectedModel = model
                                modelSearchQuery = ""
                                modelDropdownExpanded = false
                                modelError = false
                            }) {
                                Text(model)
                            }
                        }
                    }
                }
            }
            if (modelError) {
                Text("Заполните поле", color = Color.Red, style = MaterialTheme.typography.caption)
            }

            // Год выпуска
            Text("Год выпуска")
            Box {
                OutlinedTextField(
                    value = year,
                    onValueChange = {},
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
                    interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect { if (it is PressInteraction.Release) yearDropdownExpanded = true }
                        }
                    }
                )
                DropdownMenu(
                    expanded = yearDropdownExpanded,
                    onDismissRequest = { yearDropdownExpanded = false },
                    modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp)
                ) {
                    years.forEach { yearOption ->
                        DropdownMenuItem(onClick = {
                            year = yearOption
                            yearDropdownExpanded = false
                            yearError = false
                        }) {
                            Text(yearOption)
                        }
                    }
                }
            }
            if (yearError) {
                Text("Заполните поле", color = Color.Red, style = MaterialTheme.typography.caption)
            }

            // Текущий пробег
            Text("Текущий пробег")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    val currentMileage = mileage.toIntOrNull() ?: 0
                    if (currentMileage >= 1000) mileage = (currentMileage - 1000).toString()
                    mileageError = false
                }, modifier = Modifier.size(48.dp)) { Text("-") }
                OutlinedTextField(
                    value = mileage,
                    onValueChange = { if (it.all { char -> char.isDigit() } || it.isEmpty()) { mileage = it; mileageError = false } },
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
                Button(onClick = {
                    val currentMileage = mileage.toIntOrNull() ?: 0
                    mileage = (currentMileage + 1000).toString()
                    mileageError = false
                }, modifier = Modifier.size(48.dp)) { Text("+") }
            }
            if (mileageError) {
                Text("Заполните поле", color = Color.Red, style = MaterialTheme.typography.caption, modifier = Modifier.padding(start = 56.dp))
            }

            // Проверка, является ли рекомендация общей
            val isCustomCar = CarData.getRecommendations(selectedBrand, selectedModel) == CarData.getRecommendations("Custom", "Custom")
            if (isCustomCar) {
                Text(
                    text = "Внимание - Вы выбрали свой вариант машины, рекомендации будут общего характера",
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            // Рекомендации

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                color = getColorFromResources(R.color.main_color),
                shape = RoundedCornerShape(8.dp) // Закругление углов
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val recommendation = CarData.getRecommendations(selectedBrand, selectedModel)
                    Text(
                        text = "Рекомендации по обслуживанию:",
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text("Замена масла: каждые ${recommendation.oilChangeIntervalKm} км")
                    Text("Полная проверка: каждые ${recommendation.fullInspectionIntervalKm} км")
                    Text("Ротация шин: каждые ${recommendation.tireRotationIntervalKm} км")
                    Text("Проверка тормозов: каждые ${recommendation.brakeCheckIntervalKm} км")
                }
            }


            // Кнопки
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                ) {
                    Text("Закрыть", color = Color.White)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        // Сбрасываем все ошибки перед проверкой
                        nameError = name.isEmpty()
                        kmError = selectedKm.isEmpty()
                        brandError = selectedBrand.isEmpty()
                        modelError = selectedModel.isEmpty()
                        yearError = year.isEmpty()
                        mileageError = mileage.isEmpty()

                        if (!nameError && !kmError && !brandError && !modelError && !yearError && !mileageError) {
                            with(sharedPreferences.edit()) {
                                putString("name", name)
                                putString("avgKm", selectedKm)
                                putString("brand", selectedBrand)
                                putString("model", selectedModel)
                                putString("year", year)
                                putString("mileage", mileage)
                                apply()
                            }
                            surveyDataState.value = SurveyData(name, selectedKm, selectedBrand, selectedModel, year, mileage)
                            Toast.makeText(context, "Данные сохранены", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
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