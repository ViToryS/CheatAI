package com.example.cheatai.data

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val description: String,
    val coverUrl: String,
    val pages: Int,

    val sourceType: SourceType = SourceType.STATIC,
    val filePath: String? = null,
    val chapters: List<BookChapter> = emptyList()
)

enum class SourceType {
    STATIC,
}

data class BookChapter(
    val id: String,
    val title: String,
    val content: String,
    val order: Int
)