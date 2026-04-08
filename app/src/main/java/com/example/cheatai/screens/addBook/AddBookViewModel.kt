package com.example.cheatai.screens.addBook

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheatai.data.database.entities.BookEntity
import com.example.cheatai.data.repository.BookRepository
import com.example.cheatai.utils.FileHelper
import com.example.cheatai.utils.ReadiumHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddBookViewModel(
    private val repository: BookRepository,
    private val contentResolver: ContentResolver,
    private val context: Context
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _author = MutableStateFlow("Неизвестный автор")
    val author: StateFlow<String> = _author.asStateFlow()

    private val _selectedFileUri = MutableStateFlow<Uri?>(null)
    val selectedFileUri: StateFlow<Uri?> = _selectedFileUri.asStateFlow()

    private val _fileName = MutableStateFlow<String?>(null)
    val fileName: StateFlow<String?> = _fileName.asStateFlow()

    private val _coverUri = MutableStateFlow<Uri?>(null)
    val coverUri: StateFlow<Uri?> = _coverUri.asStateFlow()

    private val _isLoadingMetadata = MutableStateFlow(false)
    val isLoadingMetadata: StateFlow<Boolean> = _isLoadingMetadata.asStateFlow()

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateDescription(newDescription: String) {
        _description.value = newDescription
    }

    fun updateAuthor(newAuthor: String) {
        _author.value = newAuthor
    }
    fun updateSelectedFileUri(uri: Uri?) { _selectedFileUri.value = uri }
    fun updateFileName(name: String?) { _fileName.value = name }
    fun updateCoverUri(uri: Uri?) { _coverUri.value = uri }
    fun parseEpubMetadata(uri: Uri) {
        viewModelScope.launch {
            _isLoadingMetadata.value = true
            try {
                val tempFile = FileHelper.copyEpubToTempFile(uri, contentResolver, context)
                val publication = ReadiumHelper.openPublication(tempFile, contentResolver, context)

                publication.metadata.title?.let { _title.value = it }
                publication.metadata.authors.joinToString { it.name }.let { _author.value = it }
                publication.metadata.description?.let { _description.value = it }

                val coverBitmap = ReadiumHelper.getCoverBitmap(publication, context)
                if (coverBitmap != null) {
                    val coverFile = FileHelper.saveBitmapToTempFile(coverBitmap, context)
                    _coverUri.value = coverFile.toUri()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoadingMetadata.value = false
            }
        }
    }
    fun saveBook(onSuccess: () -> Unit) {
        when {
            _title.value.isBlank() -> {
                Toast.makeText(context, "Пожалуйста, введите название книги", Toast.LENGTH_LONG).show()
            }
            _selectedFileUri.value == null -> {
                Toast.makeText(context, "Пожалуйста, выберите файл книги (формат EPUB)", Toast.LENGTH_LONG).show()
            }
            else -> {
                viewModelScope.launch {
                    val coverPath = if (_coverUri.value != null) {
                        FileHelper.saveCoverToInternalStorage(_coverUri.value!!, contentResolver, context)
                    } else {
                        "drawable://default_cover"
                    }
                    val savedFilePath = FileHelper.copyEpubToInternalStorage(
                        _selectedFileUri.value!!, contentResolver, context
                    )
                    val bookEntity = BookEntity(
                        title = _title.value,
                        author = _author.value,
                        description = _description.value,
                        coverPath = coverPath,
                        filePath = savedFilePath,
                        fileUri = _selectedFileUri.value.toString(),
                        totalPages = 0,
                        currentPage = 0
                    )
                    repository.saveBook(bookEntity)
                    onSuccess()
                }
            }
        }
    }
}