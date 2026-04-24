package com.example.cheatai.screens.reader
import android.content.ContentResolver
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheatai.BuildConfig
import com.example.cheatai.data.database.entities.BookEntity
import com.example.cheatai.data.repository.BookRepository


import com.example.cheatai.utils.ReadiumHelper
import com.example.cheatai.utils.WikidataApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.readium.r2.shared.publication.Locator
import org.readium.r2.shared.publication.Publication
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class ReaderViewModel(
    private val repository: BookRepository,
    private val contentResolver: ContentResolver,
    private val context: Context
) : ViewModel() {

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _searchMessage = MutableStateFlow<String?>(null)
    val searchMessage: StateFlow<String?> = _searchMessage.asStateFlow()

    private val _extractedPlaces = MutableStateFlow<List<String>>(emptyList())
    val extractedPlaces: StateFlow<List<String>> = _extractedPlaces.asStateFlow()

    private val _isExtracting = MutableStateFlow(false)
    val isExtracting: StateFlow<Boolean> = _isExtracting.asStateFlow()

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

    private val _wordDefinition = MutableStateFlow<String?>(null)
    val wordDefinition: StateFlow<String?> = _wordDefinition.asStateFlow()

    private val _selectedPlace = MutableStateFlow("")
    val selectedPlace: StateFlow<String> = _selectedPlace.asStateFlow()

    private val _isSearchingWord = MutableStateFlow(false)
    val isSearchingWord: StateFlow<Boolean> = _isSearchingWord.asStateFlow()

    private val _definition = MutableStateFlow<String?>(null)
    val definition: StateFlow<String?> = _definition.asStateFlow()

    private val _isLoadingDefinition = MutableStateFlow(false)
    val isLoadingDefinition: StateFlow<Boolean> = _isLoadingDefinition.asStateFlow()

    fun showSearchMessage(message: String) {
        _searchMessage.value = message
        viewModelScope.launch {
            delay(4000)
            _searchMessage.value = null
        }
    }

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
            Locator.fromJSON(JSONObject(initialLocatorJson))
        } else {
            lastLocator?.let { Locator.fromJSON(JSONObject(it)) }
        }
    }
    fun extractPlacesFromText(text: String) {
        viewModelScope.launch {
            _isSearching.value = true
            _searchMessage.value = null
            _isExtracting.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    sendPostRequest(text)
                }
                _extractedPlaces.value = result
                showSearchMessage("Найдено ${result.size} мест в главе")
                println("НАЙДЕННЫЕ МЕСТА")
                result.forEach { println("• $it") }
            } catch (e: Exception) {
                showSearchMessage("Ошибка при поиске мест")
                println("Ошибка при запросе: ${e.message}")
                _extractedPlaces.value = emptyList()
            } finally {
                _isExtracting.value = false
                _isSearching.value = false
            }
        }
    }
    private fun sendPostRequest(text: String): List<String> {
        val url = URL("http://${BuildConfig.MY_IP}:8000/extract")
        val connection = url.openConnection() as HttpURLConnection

        return try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            connection.doOutput = true


            val escapedText = text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
            val jsonInput = "{\"text\":\"$escapedText\"}"

            connection.outputStream.use { os ->
                os.write(jsonInput.toByteArray(Charsets.UTF_8))
            }

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                val placesArray = json.getJSONArray("places")
                val places = mutableListOf<String>()
                for (i in 0 until placesArray.length()) {
                    places.add(placesArray.getString(i))
                }
                places
            } else {
                println("Ошибка сервера: $responseCode")
                emptyList()
            }
        } catch (e: Exception) {
            println("Ошибка соединения: ${e.message}")
            emptyList()
        } finally {
            connection.disconnect()
        }
    }


    suspend fun getCurrentChapterText(locator: Locator?): String {
        if (locator == null) return ""

        return try {
            val href = locator.href ?: return ""
            val resource = _publication.value?.get(href)
            val bytes = resource?.read()?.getOrNull()

            if (bytes != null) {
                val rawText = String(bytes, Charsets.UTF_8)

                stripHtml(rawText)
            } else {
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
    private fun stripHtml(html: String): String {

        return html.replace(Regex("<[^>]*>"), "")
            .replace(Regex("\\s+"), " ")

            .trim()
    }
    fun updateSelectedPlace(place: String) {
        _selectedPlace.value = place
    }

    fun updateSelectedWord(word: String) {
        _selectedWord.value = word
    }

    fun fetchWordDefinition(word: String) {
        viewModelScope.launch {
            _isLoadingDefinition.value = true
            _definition.value = null

            try {
                val result = withContext(Dispatchers.IO) {
                    WikidataApiService.getFullDescription(word)
                }
                _definition.value = result
            } catch (e: Exception) {
                _definition.value = "Ошибка при загрузке определения: ${e.message}"
            } finally {
                _isLoadingDefinition.value = false
            }
        }
    }
    fun clearExtractedPlaces() {
        _extractedPlaces.value = emptyList()
    }


}


