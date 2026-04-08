package com.example.cheatai.screens.description

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheatai.data.database.entities.BookEntity
import com.example.cheatai.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DescriptionViewModel(
    private val repository: BookRepository
) : ViewModel() {

    private val _book = MutableStateFlow<BookEntity?>(null)
    val book: StateFlow<BookEntity?> = _book.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadBook(bookId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            val bookEntity = repository.getBookById(bookId).first()
            _book.value = bookEntity
            _isLoading.value = false
        }
    }
}