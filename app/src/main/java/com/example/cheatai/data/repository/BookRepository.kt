package com.example.cheatai.data.repository

import com.example.cheatai.data.database.CheatAIDatabase
import com.example.cheatai.data.database.entities.BookEntity
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class BookRepository @Inject constructor(
    private val database: CheatAIDatabase
) {

    fun getAllBooks(): Flow<List<BookEntity>> {
        return database.bookDao().getAllBooks()
    }

    suspend fun saveBook(book: BookEntity) {
        database.bookDao().insertBook(book)
    }

    fun getBookById(bookId: Long): Flow<BookEntity?> {
        return database.bookDao().getBookByIdFlow(bookId)
    }

    suspend fun updateLocator(bookId: Long, locator: String) {
        database.bookDao().updateLocator(bookId, locator)
    }

    suspend fun deleteBookById(bookId: Long) {
        database.bookDao().deleteBookById(bookId)
    }
}