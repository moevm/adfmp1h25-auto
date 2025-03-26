package ru.etu.auto.components

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.R as AndroidR

@Composable
fun DateInputField(
    label: String,
    initialDate: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    val context = LocalContext.current
    var date by remember { mutableStateOf(initialDate) }
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val brandColor = Color(0xFF6495ED)

    OutlinedTextField(
        value = date,
        onValueChange = { /* Read-only field */ },
        label = { Text(label) },
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            focusedBorderColor = brandColor.copy(alpha = 0.7f),
            unfocusedBorderColor = brandColor.copy(alpha = 0.5f),
            disabledBorderColor = brandColor.copy(alpha = 0.3f),
            focusedLabelColor = brandColor.copy(alpha = 0.7f),
            unfocusedLabelColor = brandColor.copy(alpha = 0.5f),
            disabledLabelColor = brandColor.copy(alpha = 0.3f),
            disabledTextColor = Color.Gray
        ),
        modifier = modifier.clickable(
            enabled = isEnabled,
            onClick = {
                val currentDate = LocalDate.now()
                DatePickerDialog(
                    context,
                    AndroidR.style.Theme_Material_Dialog,
                    { _, year, month, dayOfMonth ->
                        val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                        val formattedDate = selectedDate.format(formatter)
                        date = formattedDate
                        onDateSelected(formattedDate)
                    },
                    currentDate.year,
                    currentDate.monthValue - 1,
                    currentDate.dayOfMonth
                ).show()
            }
        ),
        enabled = isEnabled,
        readOnly = true
    )
}