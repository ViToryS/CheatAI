package com.example.cheatai.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.cheatai.screens.MainScreen
import com.example.cheatai.screens.AddBookScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {

        composable("main") {
            MainScreen(navController)
        }

        composable("add_screen") {
            AddBookScreen()
        }
    }
}