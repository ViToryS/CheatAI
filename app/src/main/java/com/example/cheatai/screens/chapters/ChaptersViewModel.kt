package com.example.cheatai.screens.chapters

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheatai.data.repository.BookRepository
import com.example.cheatai.utils.ReadiumHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.readium.r2.shared.publication.Link
import org.readium.r2.shared.publication.Publication
import java.io.File




class ChaptersViewModel(
    private val repository: BookRepository,
    private val contentResolver: ContentResolver,
    private val context: Context
) : ViewModel() {


    private val _chapters = MutableStateFlow<List<Chapter>>(emptyList())
    val chapters: StateFlow<List<Chapter>> = _chapters.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _publication = MutableStateFlow<Publication?>(null)
    val publication: StateFlow<Publication?> = _publication.asStateFlow()

    fun loadChapters(bookId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val bookEntity = repository.getBookById(bookId.toLong()).first()
                val filePath = bookEntity?.filePath

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

                val tableOfContents = publication.tableOfContents

                fun linkToChapter(link: Link, level: Int = 0): Chapter {
                    return Chapter(
                        id = link.url().toString(),
                        title = link.title ?: "Глава ${level + 1}",
                        file = link.url().toString(),
                        level = level + 1,
                        isSubChapter = level > 0,
                        subchapters = link.children.map { child ->
                            linkToChapter(child, level + 1)
                        }
                    )
                }

                _chapters.value = tableOfContents.map { linkToChapter(it) }
                _isLoading.value = false

            } catch (e: Exception) {
                _errorMessage.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun onChapterClick(bookId: String, index: Int, onNavigate: (String) -> Unit) {
        val pub = _publication.value
        if (pub != null && index < pub.tableOfContents.size) {
            val link = pub.tableOfContents[index]
            val locator = pub.locatorFromLink(link)
            val locatorJson = locator?.toJSON().toString()
            onNavigate("reader/$bookId?locator=${Uri.encode(locatorJson)}")
        }
    }
}