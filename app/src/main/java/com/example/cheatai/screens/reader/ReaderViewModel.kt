package com.example.cheatai.screens.reader
import android.content.ContentResolver
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheatai.data.database.entities.BookEntity
import com.example.cheatai.data.repository.BookRepository
import com.example.cheatai.utils.ReadiumHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.readium.r2.shared.publication.Locator
import org.readium.r2.shared.publication.Publication
import java.io.File

class ReaderViewModel(
    private val repository: BookRepository,
    private val contentResolver: ContentResolver,
    private val context: Context
) : ViewModel() {

    private val _publication = MutableStateFlow<Publication?>(null)
    val publication: StateFlow<Publication?> = _publication.asStateFlow()

    private val _bookEntity = MutableStateFlow<BookEntity?>(null)
    val bookEntity: StateFlow<BookEntity?> = _bookEntity.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _showTopPanel = MutableStateFlow(false)
    val showTopPanel: StateFlow<Boolean> = _showTopPanel.asStateFlow()

    private val _showBottomPanel = MutableStateFlow(false)
    val showBottomPanel: StateFlow<Boolean> = _showBottomPanel.asStateFlow()

    private val _mapSelectionMode = MutableStateFlow(false)
    val mapSelectionMode: StateFlow<Boolean> = _mapSelectionMode.asStateFlow()

    private val _defSelectionMode = MutableStateFlow(false)
    val defSelectionMode: StateFlow<Boolean> = _defSelectionMode.asStateFlow()

    private val _currentLocator = MutableStateFlow<Locator?>(null)
    val currentLocator: StateFlow<Locator?> = _currentLocator.asStateFlow()

    private val _selectedWord = MutableStateFlow("")
    val selectedWord: StateFlow<String> = _selectedWord.asStateFlow()

    private val _selectedPlace = MutableStateFlow("")
    val selectedPlace: StateFlow<String> = _selectedPlace.asStateFlow()


    fun togglePanels() {
        _showTopPanel.value = !_showTopPanel.value
        _showBottomPanel.value = !_showBottomPanel.value
        if (!_showTopPanel.value) {
            _mapSelectionMode.value = false
            _defSelectionMode.value = false
        }
    }

    fun toggleMapSelection() {
        _mapSelectionMode.value = !_mapSelectionMode.value
        if (_mapSelectionMode.value) {
            _defSelectionMode.value = false
            _selectedPlace.value = ""
        }
    }

    fun toggleDefSelection() {
        _defSelectionMode.value = !_defSelectionMode.value
        if (_defSelectionMode.value) {
            _mapSelectionMode.value = false
            _selectedWord.value = ""
        }
    }

    fun loadBook(bookId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val book = repository.getBookById(bookId).first()
                _bookEntity.value = book

                val filePath = book?.filePath
                if (filePath.isNullOrEmpty()) {
                    _errorMessage.value = "Файл книги не найден"
                    _isLoading.value = false
                    return@launch
                }

                val file = File(filePath)
                if (!file.exists()) {
                    _errorMessage.value = "Файл книги удален"
                    _isLoading.value = false
                    return@launch
                }

                val publication = ReadiumHelper.openPublication(file, contentResolver, context)
                _publication.value = publication
                _isLoading.value = false

            } catch (e: Exception) {
                _errorMessage.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun saveReadingPosition(bookId: Long, locatorJson: String) {
        viewModelScope.launch {
            repository.updateLocator(bookId, locatorJson)
        }
    }

    fun updateCurrentLocator(locator: Locator) {
        _currentLocator.value = locator
    }
    fun getInitialLocator(initialLocatorJson: String, lastLocator: String?): Locator? {
        return if (initialLocatorJson.isNotEmpty()) {
            Locator.fromJSON(org.json.JSONObject(initialLocatorJson))
        } else {
            lastLocator?.let { Locator.fromJSON(org.json.JSONObject(it)) }
        }
    }
}