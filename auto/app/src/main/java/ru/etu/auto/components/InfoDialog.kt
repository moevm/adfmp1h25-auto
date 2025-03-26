package ru.etu.auto.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.etu.auto.R
import ru.etu.auto.shared.getColorFromResources

@Composable
fun InfoDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(getColorFromResources(R.color.sub_main_color).copy(alpha = 0.6f))
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp)
                .background(
                    color = getColorFromResources(R.color.main_color).copy(alpha = 0.95f),
                    shape = MaterialTheme.shapes.medium
                ),
            title = {
                Text(
                    text = "Authors",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        "Nasonov Yaroslav",
                        "Ivanov Artur",
                        "Popandopulo Alexander"
                    ).forEach { author ->
                        Text(
                            text = author,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                }
            },
            buttons = {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                ) {
                    Text(
                        text = "Close",
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        )
    }
}