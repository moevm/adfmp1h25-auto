package com.example.testauto.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.testauto.components.CustomTopBar
import com.example.testauto.components.InfoDialog
@Composable
fun HomeScreen(
    navController: NavHostController,
) {
    var showInfo by remember { mutableStateOf(false) }
    if (showInfo) {
        InfoDialog { showInfo = false }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Главная",
                onInfoClick = { showInfo = true },
                onProfileClick = { navController.navigate("survey") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val cardModifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

            Button(
                onClick = {
                    navController.navigate("reminders") {
                        popUpTo("home") { saveState = true; inclusive = false }
                        launchSingleTop = true
                    }
                },
                modifier = cardModifier.height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Добавить напоминание")
            }
        }
    }
}
