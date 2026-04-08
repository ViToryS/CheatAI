package com.example.cheatai.screens.notes


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cheatai.data.database.entities.toDomainBook
import com.example.cheatai.data.model.Book
import com.example.cheatai.data.model.Note
import com.example.cheatai.data.model.NoteLocation
import com.example.cheatai.data.repository.BookRepository
import com.example.cheatai.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.readium.r2.shared.publication.Locator

class NotesViewModel(
    private val noteRepository: NoteRepository,
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _isAddingNewNote = MutableStateFlow(false)
    val isAddingNewNote: StateFlow<Boolean> = _isAddingNewNote.asStateFlow()

    private val _newNoteTitle = MutableStateFlow("")
    val newNoteTitle: StateFlow<String> = _newNoteTitle.asStateFlow()

    private val _newNoteContent = MutableStateFlow("")
    val newNoteContent: StateFlow<String> = _newNoteContent.asStateFlow()

    private val _notesList = MutableStateFlow<List<Note>>(emptyList())
    val notesList: StateFlow<List<Note>> = _notesList.asStateFlow()

    private val _refreshTrigger = MutableStateFlow(false)
    val refreshTrigger: StateFlow<Boolean> = _refreshTrigger.asStateFlow()

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun startAddingNote() {
        _isAddingNewNote.value = true
    }

    fun cancelAddNote() {
        _isAddingNewNote.value = false
        _newNoteTitle.value = ""
        _newNoteContent.value = ""
    }

    fun updateNewNoteTitle(title: String) {
        _newNoteTitle.value = title
    }

    fun updateNewNoteContent(content: String) {
        _newNoteContent.value = content
    }

    fun loadBook(bookId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val bookEntity = bookRepository.getBookById(bookId).first()
                _book.value = bookEntity?.toDomainBook()
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadNotes(bookId: String) {
        viewModelScope.launch {
            noteRepository.getNotesForBook(bookId).collect { notes ->
                _notesList.value = notes
            }
        }
    }

    private fun triggerRefresh() {
        _refreshTrigger.value = !_refreshTrigger.value
    }

    fun createNewNote(
        bookId: String,
        title: String,
        content: String,
        location: NoteLocation
    ) {
        viewModelScope.launch {
            val newNote = Note(
                title = title,
                content = content,
                bookId = bookId,
                location = location
            )
            noteRepository.addNote(newNote)
            triggerRefresh()
            cancelAddNote()
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteRepository.deleteNote(note.id)
            triggerRefresh()
        }
    }

    fun updateNote(updatedNote: Note) {
        viewModelScope.launch {
            noteRepository.updateNote(updatedNote)
            triggerRefresh()
        }
    }

    fun getLocationText(source: String, initialLocatorJson: String): String {
        return if (source == "reader" && initialLocatorJson.isNotEmpty()) {
            try {
                val locator = Locator.fromJSON(org.json.JSONObject(initialLocatorJson))
                val totalProgression = locator?.locations?.totalProgression
                if (totalProgression != null) {
                    val percent = (totalProgression * 100).toInt()
                    "Поз. $percent%"
                } else {
                    "Позиция в книге"
                }
            } catch (e: Exception) {
                "Позиция в книге"
            }
        } else {
            "О книге..."
        }
    }
}