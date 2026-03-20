package com.example.cheatai.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cheatai.screens.AddBookScreen
import com.example.cheatai.screens.BookDescriptionScreen
import com.example.cheatai.screens.ChaptersScreen
import com.example.cheatai.screens.MainScreen
import com.example.cheatai.screens.NotesScreen
import com.example.cheatai.screens.ReaderScreen


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
            AddBookScreen(navController)
        }

        composable("book_description/{bookId}") { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            BookDescriptionScreen(bookId, navController)
        }

        composable("book_notes/{bookId}/{source}") { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: return@composable
            val source = backStackEntry.arguments?.getString("source") ?: "description"
            NotesScreen(
                navController = navController,
                bookId = bookId,
                source = source
            )
        }

        composable("reader/{bookId}/{source}") { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            val source = backStackEntry.arguments?.getString("source") ?: "description"
            ReaderScreen(
                navController = navController,
                bookId = bookId,
                source = source
            )
        }
        composable("chapters") {
            ChaptersScreen(
                navController = navController,
                onChapterClick = { chapter ->
                    navController.navigate("reader/1/0")
                }
            )
        }
    }
}