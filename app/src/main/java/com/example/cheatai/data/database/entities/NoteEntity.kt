package com.example.cheatai.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cheatai.data.model.Note
import com.example.cheatai.data.model.NoteLocation
import org.json.JSONObject
import org.readium.r2.shared.publication.Locator

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val bookId: String,
    val locationType: String,
    val locationValue: Int,
    val locationPosition: Float,
    val locationLocator: String?,
    val createdAt: Long,
    val updatedAt: Long
)

fun NoteEntity.toDomainNote(): Note {
    val location = when (locationType) {
        "page" -> NoteLocation.ByPage(locationValue)
        "chapter" -> NoteLocation.ByChapter(locationValue)
        "position" -> NoteLocation.ByPosition(locationPosition)
        "locator" -> {
            val locator = Locator.fromJSON(JSONObject(locationLocator ?: "{}"))
            NoteLocation.ByLocator(locator)
        }
        else -> NoteLocation.ByPosition(0f)
    }
    return Note(
        id = id,
        title = title,
        content = content,
        bookId = bookId,
        location = location,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        bookId = bookId,
        locationType = when (location) {
            is NoteLocation.ByPage -> "page"
            is NoteLocation.ByChapter -> "chapter"
            is NoteLocation.ByPosition -> "position"
            is NoteLocation.ByLocator -> "locator"
        },
        locationValue = when (location) {
            is NoteLocation.ByPage -> location.page
            is NoteLocation.ByChapter -> location.chapterIndex
            else -> -1
        },
        locationPosition = when (location) {
            is NoteLocation.ByPosition -> location.position
            else -> 0f
        },
        locationLocator = when (location) {
            is NoteLocation.ByLocator -> location.locator?.toJSON().toString()
            else -> null
        },
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}