package ru.etu.auto.components

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.R as AndroidR

@Composable
fun DateInputField(
    label: String,
    date: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dateState = remember { mutableStateOf(date) }
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    OutlinedTextField(
        value = dateState.value,
        onValueChange = { /* Игнорируем прямой ввод */ },
        label = { Text(label) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF6495ED).copy(alpha = 0.7f),
            unfocusedBorderColor = Color(0xFF6495ED).copy(alpha = 0.7f),
            disabledBorderColor = Color(0xFF6495ED).copy(alpha = 0.7f),
            errorBorderColor = Color(0xFF6495ED).copy(alpha = 0.7f),
            focusedLabelColor = Color(0xFF6495ED).copy(alpha = 0.7f),
            unfocusedLabelColor = Color(0xFF6495ED).copy(alpha = 0.7f),
            disabledTextColor = Color.White
        ),
        modifier = modifier
            .clickable {
                val now = LocalDate.now()
                val datePicker = DatePickerDialog(
                    context,
                    AndroidR.style.Theme_Material_Dialog,
                    { _, year, month, dayOfMonth ->
                        val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                        val formattedDate = selectedDate.format(formatter)
                        dateState.value = formattedDate
                        onDateSelected(formattedDate)
                    },
                    now.year,
                    now.monthValue - 1,
                    now.dayOfMonth
                )
                datePicker.show()
            },
        enabled = false,
        readOnly = true
    )
}