package com.example.cheatai.data.repository

import com.example.cheatai.data.database.CheatAIDatabase
import com.example.cheatai.data.database.entities.toDomainNote
import com.example.cheatai.data.database.entities.toEntity
import com.example.cheatai.data.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepository(
    private val database: CheatAIDatabase
) {

    fun getNotesForBook(bookId: String): Flow<List<Note>> {
        return database.noteDao().getNotesForBook(bookId)
            .map { entities ->
                entities.map { it.toDomainNote() }
            }
    }

    suspend fun addNote(note: Note) {
        database.noteDao().insertNote(note.toEntity())
    }

    suspend fun updateNote(note: Note) {
        database.noteDao().updateNote(note.toEntity())
    }

    suspend fun deleteNote(noteId: String) {
        database.noteDao().deleteNoteById(noteId)
    }
}