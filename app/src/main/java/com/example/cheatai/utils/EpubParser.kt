package com.example.cheatai.utils

import android.content.Context
import androidx.annotation.RawRes
import com.google.gson.Gson


data class BookConfig(
    val bookTitle: String,
    val author: String,
    val chapters: List<ChapterConfig>
)

data class ChapterConfig(
    val id: Int,
    val title: String,
    val file: String,
    val level: Int = 1,
    val subchapters: List<ChapterConfig> = emptyList()
)

data class Chapter(
    val id: String,
    val title: String,
    val file: String,
    val level: Int,
    val isSubChapter: Boolean = level > 1
)

class ChaptersParser(private val context: Context) {

    private val gson = Gson()


    fun loadChaptersFromResource(@RawRes rawResId: Int): List<Chapter> {
        return try {
            val jsonString = context.resources.openRawResource(rawResId)
                .bufferedReader()
                .use { it.readText() }
            val config = gson.fromJson(jsonString, BookConfig::class.java)

            val result = mutableListOf<Chapter>()

            config.chapters.forEach { chapterConfig ->
                result.add(
                    Chapter(
                        id = "chap_${chapterConfig.id}",
                        title = chapterConfig.title,
                        file = chapterConfig.file,
                        level = chapterConfig.level,
                        isSubChapter = false
                    )
                )
                chapterConfig.subchapters.forEach { subConfig ->
                    result.add(
                        Chapter(
                            id = "chap_${subConfig.id}",
                            title = subConfig.title,
                            file = subConfig.file,
                            level = subConfig.level,
                            isSubChapter = true
                        )
                    )
                }
            }

            result
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun getBookInfo(@RawRes rawResId: Int): Pair<String, String>? {
        return try {
            val jsonString = context.resources.openRawResource(rawResId)
                .bufferedReader()
                .use { it.readText() }

            val config = gson.fromJson(jsonString, BookConfig::class.java)
            Pair(config.bookTitle, config.author)
        } catch (e: Exception) {
            null
        }
    }
}