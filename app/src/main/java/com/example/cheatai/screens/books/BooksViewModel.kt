package com.example.cheatai.screens.books
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheatai.data.database.entities.BookEntity
import com.example.cheatai.data.database.entities.toDomainBook
import com.example.cheatai.data.model.Book
import com.example.cheatai.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BooksViewModel(
    private val repository: BookRepository
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    private val _books: StateFlow<List<BookEntity>> = repository.getAllBooks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val domainBooks: StateFlow<List<Book>> = _books.map { entities ->
        entities.map { it.toDomainBook() }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            _books.collect {
                _isLoading.value = false
            }
        }
    }


    suspend fun getBookById(bookId: Long): BookEntity? {
        return repository.getBookById(bookId).first()
    }

    fun saveReadingPosition(bookId: Long, locator: String) {
        viewModelScope.launch {
            repository.updateLocator(bookId, locator)
        }
    }
}