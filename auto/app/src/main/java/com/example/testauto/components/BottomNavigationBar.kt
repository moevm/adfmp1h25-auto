package com.example.testauto.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.testauto.R
import com.example.testauto.models.Screen
import com.example.testauto.ui.theme.getColorFromResources
import kotlinx.coroutines.flow.map

@Composable
fun BottomNavigationBar(navController: NavHostController, items: List<Screen>) {
    BottomNavigation(
        backgroundColor = getColorFromResources(R.color.main_color), // Цвет фона всего меню (#001F54)
        contentColor = Color.White,
        elevation = 8.dp
    ) {
        val currentRoute by navController.currentBackStackEntryFlow
            .map { it?.destination?.route }
            .collectAsState(initial = null)

        // Отладка: текущий маршрут и стек навигации
        LaunchedEffect(currentRoute) {
            Log.d("BottomNav", "Current route: $currentRoute")
            Log.d("BottomNav", "Back stack: ${navController.backQueue.map { it.destination.route }}")
        }

        items.forEach { screen ->
            val isSelected = currentRoute == screen.route
            Log.d("BottomNav", "Screen: ${screen.route}, isSelected: $isSelected")
            val alphaValue = if (isSelected) 0f else 0.3f
            Log.d("BottomNav", "Screen: ${screen.route}, alpha: $alphaValue")

            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = screen.label, tint = Color.White) },
                label = { Text(screen.label, color = Color.White) },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        // Очищаем стек до home и восстанавливаем его как активный маршрут
                        popUpTo(Screen.Home.route) {
                            saveState = true
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                modifier = Modifier
                    .background(color = Color.White.copy(alpha = alphaValue))
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White
            )
        }
    }
}