package ru.etu.auto.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomNavigation
import androidx.compose.material3.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.etu.auto.models.Screen
import ru.etu.auto.shared.getColorFromResources
import kotlinx.coroutines.flow.map
import ru.etu.auto.R

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    items: List<Screen>,
    modifier: Modifier = Modifier
) {
    BottomNavigation(
        backgroundColor = getColorFromResources(R.color.main_color),
        contentColor = Color.White,
        elevation = 8.dp,
        modifier = modifier
    ) {
        val currentRoute by navController.currentBackStackEntryFlow
            .map { it?.destination?.route }
            .collectAsState(initial = null)

        // Debug logging
        LaunchedEffect(currentRoute) {
            if (BuildConfig.DEBUG) {
                Log.d("BottomNav", "Current route: $currentRoute")
                Log.d("BottomNav", "Back stack: ${navController.backQueue.map { it.destination.route }}")
            }
        }

        items.forEach { screen ->
            val isSelected = currentRoute == screen.route

            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.label,
                        tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
                    )
                },
                label = {
                    Text(
                        text = screen.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
                    )
                },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp),
            )
        }
    }
}

