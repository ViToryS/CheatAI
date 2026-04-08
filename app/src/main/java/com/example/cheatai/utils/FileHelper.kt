package com.example.cheatai.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

object FileHelper {

    fun getFileName(uri: Uri, contentResolver: ContentResolver): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex)
        }
    }

    fun copyEpubToInternalStorage(
        uri: Uri,
        contentResolver: ContentResolver,
        context: Context
    ): String {
        val inputStream = contentResolver.openInputStream(uri) ?: return ""
        val fileName = "book_${System.currentTimeMillis()}.epub"
        val outputFile = File(context.filesDir, "books/$fileName")
        outputFile.parentFile?.mkdirs()

        outputFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }

        return outputFile.absolutePath
    }

    fun copyEpubToTempFile(
        uri: Uri,
        contentResolver: ContentResolver,
        context: Context
    ): File {
        val inputStream = contentResolver.openInputStream(uri) ?: throw Exception("Can't open stream")
        val tempFile = File(context.cacheDir, "temp_${System.currentTimeMillis()}.epub")
        tempFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        return tempFile
    }

    fun saveBitmapToTempFile(bitmap: Bitmap, context: Context): File {
        val file = File(context.cacheDir, "cover_${System.currentTimeMillis()}.jpg")
        file.outputStream().use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        }
        return file
    }

    fun saveCoverToInternalStorage(uri: Uri, contentResolver: ContentResolver, context: Context): String {
        val inputStream = contentResolver.openInputStream(uri) ?: return "drawable://default_cover"
        val fileName = "cover_${System.currentTimeMillis()}.jpg"
        val outputFile = File(context.filesDir, "covers/$fileName")
        outputFile.parentFile?.mkdirs()

        outputFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        return outputFile.absolutePath
    }
}