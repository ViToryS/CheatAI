package com.example.cheatai.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cheatai.data.model.Book
import com.example.cheatai.data.model.SourceType

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val description: String,
    val title: String,
    val author: String,
    val coverPath: String,
    val filePath: String,
    val fileUri: String,
    val totalPages: Int,
    val currentPage: Int = 0,
    val lastLocator: String? = null
)

fun BookEntity.toDomainBook(): Book {
    return Book(
        id = id.toString(),
        title = title,
        author = author,
        description = description,
        coverUrl = coverPath,
        pages = totalPages,
        sourceType = SourceType.EPUB,
        filePath = filePath,
        chapters = emptyList()
    )
}