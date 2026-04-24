package com.example.cheatai

import android.app.Application
import com.example.cheatai.data.database.CheatAIDatabase
import com.example.cheatai.data.repository.BookRepository
import com.example.cheatai.data.repository.NoteRepository
import com.yandex.mapkit.MapKitFactory
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
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
        MapKitFactory.initialize(this)

        val database = CheatAIDatabase.getInstance(this)
        repository = BookRepository(database)
        noteRepository = NoteRepository(database)
    }
}