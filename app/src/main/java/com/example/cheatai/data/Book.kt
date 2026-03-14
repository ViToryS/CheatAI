package com.example.cheatai.data

import androidx.compose.runtime.Immutable

@Immutable
data class Book(
    val id: String,
    val title: String,
    val author: String,
    val description: String,
    val coverUrl: String,
    val pages: Int,
    val progress: Int? = null
)