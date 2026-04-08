package com.example.cheatai.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cheatai.data.database.dao.BookDao
import com.example.cheatai.data.database.dao.NoteDao
import com.example.cheatai.data.database.entities.BookEntity
import com.example.cheatai.data.database.entities.NoteEntity

@Database(
    entities = [BookEntity::class, NoteEntity::class],
    version = 8
)
abstract class CheatAIDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun noteDao(): NoteDao
    companion object {
        @Volatile
        private var INSTANCE: CheatAIDatabase? = null

        fun getInstance(context: Context): CheatAIDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CheatAIDatabase::class.java,
                    "cheatai.db"
                ).fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}