package com.example.cheatai

import android.app.Application
import com.example.cheatai.data.database.CheatAIDatabase
import com.example.cheatai.data.repository.BookRepository
import com.example.cheatai.data.repository.NoteRepository

class CheatAIApplication : Application() {

    companion object {
        lateinit var instance: CheatAIApplication
            private set

        lateinit var repository: BookRepository
        lateinit var noteRepository: NoteRepository
    }

    override fun onCreate() {
        super.onCreate()
        instance = this


        val database = CheatAIDatabase.getInstance(this)
        repository = BookRepository(database)
        noteRepository = NoteRepository(database)
    }
}