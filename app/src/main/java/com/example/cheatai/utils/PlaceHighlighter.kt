package com.example.cheatai.utils


import android.util.Log
import org.json.JSONObject
import org.readium.r2.navigator.DecorableNavigator
import org.readium.r2.navigator.Decoration
import org.readium.r2.navigator.epub.EpubNavigatorFragment
import org.readium.r2.shared.publication.Locator
import java.util.UUID

private const val TAG = "PlaceHighlighter"

object PlaceHighlighter {

    private var onWordSelectedListener: ((String) -> Unit)? = null

    fun setOnWordSelectedListener(listener: (String) -> Unit) {
        onWordSelectedListener = listener
    }

    private val decorationListener = object : DecorableNavigator.Listener {
        override fun onDecorationActivated(event: DecorableNavigator
            .OnActivatedEvent): Boolean {
            val selectedWord = event.decoration.locator.text.highlight ?: ""
            onWordSelectedListener?.invoke(selectedWord)
            return true
        }
    }

    fun createDecorations(
        places: List<String>,
        chapterText: String,
        currentLocator: Locator
    ): List<Decoration> {
        if (places.isEmpty()) {
            Log.d(TAG, "Список мест пуст")
            return emptyList()
        }

        val decorations = mutableListOf<Decoration>()
        var totalFound = 0

        places.forEach { place ->
            val contexts = findAllWordContexts(chapterText, place)

            contexts.forEach { context ->
                val locatorJson = JSONObject().apply {
                    put("href", currentLocator.href.toString())
                    put("type", currentLocator.mediaType.toString())
                    put("text", JSONObject().apply {
                        put("before", context.before)
                        put("highlight", context.word)
                        put("after", context.after)
                    })
                }
                val locator = Locator.fromJSON(locatorJson) ?: return@forEach
                decorations.add(
                    Decoration(
                        id = UUID.randomUUID().toString(),
                        locator = locator,
                        style = Decoration.Style.Highlight(
                            tint = 0xFF9C27B0.toInt(),
                            isActive = false
                        )
                    )
                )
                totalFound++
            }
        }
        Log.d(TAG, "Создано декораций: ${decorations.size} (найдено вхождений: $totalFound)")
        return decorations
    }

    suspend fun applyDecorations(
        fragment: EpubNavigatorFragment,
        decorations: List<Decoration>,
        groupName: String = "place-highlights"
    ) {
        if (decorations.isEmpty()) {
            return
        }

        if (!fragment.isAdded) {
            return
        }

        val navigator = fragment as? DecorableNavigator
        if (navigator != null) {
            navigator.addDecorationListener(groupName, decorationListener)
            navigator.applyDecorations(decorations, groupName)
        }
    }


    suspend fun clearDecorations(
        fragment: EpubNavigatorFragment,
        groupName: String = "place-highlights"
    ) {
        if (!fragment.isAdded) {
            return
        }

        val navigator = fragment as? DecorableNavigator
        if (navigator != null) {
            navigator.applyDecorations(emptyList(), groupName)
            Log.d(TAG, "Декорации очищены")
        }
    }


    private fun findAllWordContexts(text: String, word: String): List<WordContext> {
        val contexts = mutableListOf<WordContext>()
        val regex = Regex("(.{0,100})($word)(.{0,100})", RegexOption.IGNORE_CASE)
        var matchResult = regex.find(text)

        while (matchResult != null) {
            contexts.add(
                WordContext(
                    before = matchResult.groupValues[1].trim(),
                    word = matchResult.groupValues[2],
                    after = matchResult.groupValues[3].trim()
                )
            )
            matchResult = regex.find(text, matchResult.range.last + 1)
        }
        return contexts
    }
    private data class WordContext(
        val before: String,
        val word: String,
        val after: String
    )
}