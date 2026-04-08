package com.example.cheatai.screens.chapters

data class Chapter(
    val id: String,
    val title: String,
    val file: String,
    val level: Int,
    val isSubChapter: Boolean = level > 1,
    val subchapters: List<Chapter> = emptyList()
)