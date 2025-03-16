package ru.etu.auto.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry = navController.currentBackStackEntry
    return navBackStackEntry?.destination?.route
}
