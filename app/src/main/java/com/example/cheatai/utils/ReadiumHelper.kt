package com.example.cheatai.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.publication.Publication
import org.readium.r2.shared.util.ErrorException
import org.readium.r2.shared.util.asset.AssetRetriever
import org.readium.r2.shared.util.getOrElse
import org.readium.r2.shared.util.http.DefaultHttpClient
import org.readium.r2.shared.util.toUrl
import org.readium.r2.streamer.PublicationOpener
import org.readium.r2.streamer.parser.DefaultPublicationParser
import java.io.File

@OptIn(ExperimentalReadiumApi::class)
object ReadiumHelper {

    suspend fun openPublication(
        file: File,
        contentResolver: ContentResolver,
        context: Context
    ): Publication {
        val httpClient = DefaultHttpClient()
        val assetRetriever = AssetRetriever(contentResolver, httpClient)
        val url = file.toUrl()
        val asset = assetRetriever.retrieve(url).getOrElse { throw ErrorException(it) }

        val publicationParser = DefaultPublicationParser(context, httpClient, assetRetriever, null)
        val publicationOpener = PublicationOpener(publicationParser, emptyList())
        return publicationOpener.open(asset, allowUserInteraction = true)
            .getOrElse { throw ErrorException(it) }
    }

    suspend fun getCoverBitmap(publication: Publication, context: Context): Bitmap? = withContext(
        Dispatchers.IO) {
        try {
            val coverLink = publication.linkWithRel("cover") ?: return@withContext null

            val resource = publication.get(coverLink)
            val bytesResult = resource?.read()
            bytesResult?.let {
                if (it.isSuccess) {
                    BitmapFactory.decodeByteArray(bytesResult.getOrNull(), 0, bytesResult.getOrNull()?.size ?: 0)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}