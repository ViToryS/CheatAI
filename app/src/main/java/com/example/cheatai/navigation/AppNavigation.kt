package com.example.cheatai.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cheatai.screens.addBook.AddBookScreen
import com.example.cheatai.screens.description.BookDescriptionScreen
import com.example.cheatai.screens.chapters.ChaptersScreen
import com.example.cheatai.screens.books.BooksScreen
import com.example.cheatai.screens.notes.NotesScreen
import com.example.cheatai.screens.reader.ReaderScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            BooksScreen(navController)
        }

        composable("add_screen") {
            AddBookScreen(navController)
        }

        composable("book_description/{bookId}") { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            BookDescriptionScreen(bookId, navController)
        }

        composable(
            route = "book_notes/{bookId}/{source}?locator={locator}",
            arguments = listOf(
                navArgument("bookId") { type = NavType.StringType },
                navArgument("source") { type = NavType.StringType },
                navArgument("locator") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            val source = backStackEntry.arguments?.getString("source") ?: "description"
            val locatorJson = backStackEntry.arguments?.getString("locator") ?: ""
            NotesScreen(
                navController = navController,
                bookId = bookId,
                source = source,
                initialLocatorJson = locatorJson
            )
        }


        composable("chapters/{bookId}") { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            ChaptersScreen(
                navController = navController,
                bookId = bookId
            )
        }
        composable(
            route = "reader/{bookId}?locator={locator}",
            arguments = listOf(
                navArgument("bookId") { type = NavType.StringType },
                navArgument("locator") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: "1"
            val locatorJson = backStackEntry.arguments?.getString("locator") ?: ""
            ReaderScreen(
                navController = navController,
                bookId = bookId,
                initialLocatorJson = locatorJson
            )
        }
    }
}