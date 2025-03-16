package com.example.testauto.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.testauto.R
import com.example.testauto.ui.theme.getColorFromResources

@Composable
fun CustomTopBar(
    title: String,
    showBackButton: Boolean = false,
    onBack: () -> Unit = {},
    onInfoClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = R.drawable.logo,
                    contentDescription = "Логотип",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(200.dp)
                )
            }
        },
        navigationIcon = {
            if (showBackButton) {
                Row {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = onInfoClick) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Информация",
                            tint = Color.White
                        )
                    }
                }
            } else {
                IconButton(onClick = onInfoClick) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "Информация",
                        tint = Color.White
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { onProfileClick() }) { // Переход сразу при клике на иконку
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Профиль",
                    tint = Color.White
                )
            }
        },
        backgroundColor = getColorFromResources(R.color.main_color)
    )
}