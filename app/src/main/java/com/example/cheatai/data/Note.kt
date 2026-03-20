package com.example.cheatai.data

import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val bookId: String,
    val location: NoteLocation,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

sealed class NoteLocation {
    data class ByPage(val page: Int) : NoteLocation()
    data class ByPosition(val position: Float) : NoteLocation()
    data class ByChapter(val chapterIndex: Int) : NoteLocation()

    fun getDisplayText(book: Book): String {
        return when (this) {
            is ByPage -> if (page == -1) "О книге..." else "Стр. $page"
            is ByPosition -> {
                val percent = (position * 100).toInt()
                "Поз. $percent%"
            }
            is ByChapter -> "Глава $chapterIndex"
        }
    }
}