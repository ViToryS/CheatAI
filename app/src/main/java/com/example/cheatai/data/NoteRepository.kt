package com.example.cheatai.data

import androidx.compose.runtime.mutableStateListOf

class NotesRepository {
    private val _notes = mutableStateListOf<Note>()
    val notes: List<Note> = _notes

    init {
        _notes.addAll(
            listOf(
                Note(
                    title = "Интересная мысль о Гарри Поттере",
                    content = "Гарри очень похож на своего отца не только внешне, но и характером",
                    bookId = "1",
                    location = NoteLocation.ByPage(156),
                    createdAt = System.currentTimeMillis() - 86400000
                ),
                Note(
                    title = "Снейп загадочный персонаж",
                    content = "Его мотивы пока непонятны, но чувствуется глубина",
                    bookId = "1",
                    location = NoteLocation.ByPosition(0.35f),
                    createdAt = System.currentTimeMillis() - 43200000
                ),
                Note(
                    title = "Важный поворот сюжета",
                    content = "В этой главе раскрывается тайна философского камня",
                    bookId = "1",
                    location = NoteLocation.ByChapter( 15),
                    createdAt = System.currentTimeMillis() - 21600000
                ),
                Note(
                    title = "Сцена в лесу",
                    content = "Очень напряженный момент, когда Гарри встречает Волан-де-Морта" +
                            "В этой главе раскрывается тайна философского камня" +
                            "В этой главе раскрывается тайна философского камня" +
                            "В этой главе раскрывается тайна философского камня" +
                            "В этой главе раскрывается тайна философского камня" +
                            "В этой главе раскрывается тайна философского камня",
                    bookId = "1",
                    location = NoteLocation.ByPage(289),
                    createdAt = System.currentTimeMillis() - 3600000
                )
            )
        )
    }

    fun addNote(note: Note) {
        _notes.add(note)
    }

    fun updateNote(note: Note) {
        val index = _notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            _notes[index] = note.copy(updatedAt = System.currentTimeMillis())
        }
    }

    fun deleteNote(noteId: String) {
        _notes.removeIf { it.id == noteId }
    }

    fun getNotesForBook(bookId: String): List<Note> {
        return _notes.filter { it.bookId == bookId }
    }

    fun getNoteById(id: String): Note? {
        return _notes.find { it.id == id }
    }
}