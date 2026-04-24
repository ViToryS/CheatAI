package com.example.cheatai.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder

object WikidataApiService {

    suspend fun getFullDescription(word: String): String = withContext(Dispatchers.IO) {
        val encodedWord = URLEncoder.encode(word, "UTF-8")

        try {

            val searchUrl =
                "https://www.wikidata.org/w/api.php?action=wbsearchentities&search=" +
                        "$encodedWord&language=ru&uselang=ru&format=json"
            val searchResponse = URL(searchUrl).readText()
            val searchJson = JSONObject(searchResponse)
            val searchResults = searchJson.getJSONArray("search")

            if (searchResults.length() == 0) {
                println("WIKIDATA: Ничего не найдено для слова: $word")
                return@withContext "Слово '$word' не найдено."
            }

            val bestMatch = searchResults.getJSONObject(0)
            val entityId = bestMatch.getString("id")
            val label = bestMatch.optString("label", word)
            println("WIKIDATA: Выбран лучший результат: ID=$entityId, label=$label")

            val entityUrl = "https://www.wikidata.org/wiki/Special:EntityData/$entityId.json"
            val entityResponse = URL(entityUrl).readText()
            val entityJson = JSONObject(entityResponse)
            val entities = entityJson.getJSONObject("entities")
            val entity = entities.getJSONObject(entityId)
            val sitelinks = entity.optJSONObject("sitelinks")
            val ruWiki = sitelinks?.optJSONObject("ruwiki")
            val pageTitle = ruWiki?.optString("title")

            if (pageTitle == null) {
                val shortDescription = bestMatch.optString("description", "")
                return@withContext shortDescription.ifBlank { "$label — описание отсутствует" }
            }

            val encodedTitle = URLEncoder.encode(pageTitle, "UTF-8")
            val extractUrl =
                "https://ru.wikipedia.org/w/api.php?action=query&prop=" +
                        "extracts&exintro&explaintext&format=json&titles=$encodedTitle"
            val extractResponse = URL(extractUrl).readText()
            val extractJson = JSONObject(extractResponse)
            val pages = extractJson.getJSONObject("query").getJSONObject("pages")
            val page = pages.getJSONObject(pages.keys().next())
            val extract = page.optString("extract", "")

            if (extract.isNotBlank()) {
                return@withContext extract
            }

            val shortDescription = bestMatch.optString("description", "")
            return@withContext shortDescription.ifBlank { "$label — описание отсутствует" }

        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext "Ошибка при загрузке определения: ${e.message}"
        }
    }
}