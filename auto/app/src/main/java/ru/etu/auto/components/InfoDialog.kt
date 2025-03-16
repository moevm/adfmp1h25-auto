package ru.etu.auto.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.etu.auto.R
import ru.etu.auto.shared.getColorFromResources

@Composable
fun InfoDialog(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = getColorFromResources(R.color.sub_main_color).copy(alpha = 0.5f) // Прозрачный синий фон
            )
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            title = {
                Text(
                    text = "Авторы:",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Насонов Ярослав", color = Color.White)
                    Text(text = "Иванов Артур", color = Color.White)
                    Text(text = "Попандопуло Александр", color = Color.White)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.error
                    )
                ) {
                    Text("Закрыть", color = Color.White)
                }
            },
            backgroundColor = Color.Transparent,
            contentColor = Color.White
        )
    }
}
