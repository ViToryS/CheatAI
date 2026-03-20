package com.example.cheatai.screens

import android.content.Context
import android.util.Base64
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.cheatai.R
import com.example.cheatai.components.bottomBars.BottomDefBar
import com.example.cheatai.components.bottomBars.BottomMainRead
import com.example.cheatai.components.bottomBars.BottomMapBar
import com.example.cheatai.components.topBars.TopBarRead
import com.example.cheatai.ui.theme.GrayText
import com.example.cheatai.ui.theme.LocalAppDimensions
import com.example.cheatai.ui.theme.PinkRead
import com.example.cheatai.utils.ReaderHtmlProvider
import java.io.InputStream

fun getFontBase64(context: Context, fontResId: Int): String {
    val inputStream: InputStream = context.resources.openRawResource(fontResId)
    val fontBytes = inputStream.readBytes()
    return Base64.encodeToString(fontBytes, Base64.NO_WRAP)
}

@Composable
fun ReaderScreen(
    navController: NavController,
    bookId: String,
    source: String
) {

    val bookmarkingSource = "reader"
    val context = LocalContext.current
    val dimensions = LocalAppDimensions.current
    val density = LocalDensity.current.density
    val fontSizePx = dimensions.textMedium.value
    val configuration = LocalConfiguration.current
    val screenDensity = configuration.densityDpi

    var currentPage by rememberSaveable { mutableStateOf(0) }
    var totalPages by remember { mutableStateOf(1) }
    var currentChapter by rememberSaveable { mutableStateOf(0) }
    val totalChapters = remember { 1 }

    var showTopPanel by remember { mutableStateOf(false) }
    var showBottomPanel by remember { mutableStateOf(false) }
    var mapSelectionMode by rememberSaveable { mutableStateOf(false) }
    var defSelectionMode by rememberSaveable { mutableStateOf(false) }


    val backgroundColor = PinkRead
    val textColor = GrayText
    val bgHex = String.format("#%06X", (0xFFFFFF and backgroundColor.toArgb()))
    val textHex = String.format("#%06X", (0xFFFFFF and textColor.toArgb()))

    val enoRegularBase64 = remember { getFontBase64(context, R.font.eno_regular) }
    val enoBoldBase64 = remember { getFontBase64(context, R.font.eno_bold) }


    val htmlProvider = remember(
        dimensions, bgHex, textHex, fontSizePx,
        enoRegularBase64, enoBoldBase64
    ) {
        ReaderHtmlProvider(
            dimensions = dimensions,
            bgHex = bgHex,
            textHex = textHex,
            fontSizePx = fontSizePx,
            enoRegularBase64 = enoRegularBase64,
            enoBoldBase64 = enoBoldBase64
        )
    }

    var columnWidth by remember { mutableStateOf(310) }
    val chapterHtml = remember(currentChapter, mapSelectionMode, defSelectionMode) {
        if (mapSelectionMode || defSelectionMode) {
            htmlProvider.getFuncTestChapter(
                chapterNumber = 1,
                highlightPlace = mapSelectionMode,
                highlightDef = defSelectionMode
            )
        } else {
            htmlProvider.getTestChapter(currentChapter + 1)
        }
    }

    val webViewRef = remember { mutableStateOf<WebView?>(null) }
    class WebAppInterface {
        @JavascriptInterface
        fun onPageCountCalculated(count: Int, scrollStep: Int) {
            totalPages = count
            columnWidth = scrollStep
        }

        @JavascriptInterface
        fun onPageChanged(newPage: Int) {
            currentPage = newPage
        }
        @JavascriptInterface
        fun getCurrentPage(): Int {
            return currentPage
        }
    }

    fun togglePanels() {
        showTopPanel = !showTopPanel
        showBottomPanel = !showBottomPanel
        if (!showTopPanel){
            mapSelectionMode = false
            defSelectionMode = false
        }
        webViewRef.value?.post {
            val target = (currentPage * columnWidth * density).toInt()
            webViewRef.value?.scrollTo(target, 0)
        }
    }

    fun goToNextPage() {
        Log.d("", "${density}, $screenDensity")
        if (currentPage < totalPages - 1) {
            currentPage++
            webViewRef.value?.scrollTo((currentPage * columnWidth*density).toInt(), 0)


        } else if (currentChapter < totalChapters - 1) {
            currentChapter++
            currentPage = 0
        }
    }

    fun goToPreviousPage() {
        if (currentPage > 0) {
            currentPage--
            webViewRef.value?.scrollTo((currentPage * columnWidth*density).toInt(), 0)
        } else if (currentChapter > 0) {
            currentChapter--
            currentPage = 0
        }
    }

    Scaffold(
        topBar = {
            if (showTopPanel) {
                TopBarRead(
                    navController = navController,
                    bookId = bookId,
                    mapSelectionMode = mapSelectionMode,
                    defSelectionMode = defSelectionMode,
                    onToggleMapSelection ={
                        mapSelectionMode = !mapSelectionMode
                        defSelectionMode = false
                    },
                    onToggleDefSelection = {
                        defSelectionMode = !defSelectionMode
                        mapSelectionMode = false
                    },
                )
            }
        },
        bottomBar = {
            if (showBottomPanel) {
                when {
                    defSelectionMode -> BottomDefBar()
                    mapSelectionMode -> BottomMapBar()
                    else -> BottomMainRead(navController = navController, bookId = bookId, bookmarkingSource)
                }
            }
        }
    ) { paddingValues ->
        Log.d("fs", paddingValues.calculateBottomPadding().toString())
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(
                    top = paddingValues.calculateTopPadding() + dimensions.paddingMedium,
                    bottom = if (showBottomPanel) dimensions.bottomBarHeight + dimensions.paddingMedium
                    else dimensions.paddingMedium,
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                )
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL


                        settings.domStorageEnabled = true
                        settings.displayZoomControls = false
                        settings.builtInZoomControls = true

                        isHorizontalScrollBarEnabled = true
                        isVerticalScrollBarEnabled = false
                        scrollBarSize = 0
                        setBackgroundColor(android.graphics.Color.WHITE)
                        addJavascriptInterface(WebAppInterface(), "Android")

                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)

                                val js = """
    (function() {
    try {
        var screenWidth = window.innerWidth;
        var screenHeight = window.innerHeight;

        var style = document.createElement('style');
        style.id = 'reader-styles';
        style.innerHTML = 
            'body { ' +
                'height: ' + screenHeight + 'px !important; ' +
                '-webkit-column-width: ' + screenWidth + 'px !important; ' +
                '-webkit-column-gap: 0px !important; ' +
                'column-width: ' + screenWidth + 'px !important; ' +
                'column-gap: 0px !important; ' +
                'margin: 0 !important; ' +
                'padding: 0 !important; ' +
                'overflow-x: auto !important; ' +
                'overflow-y: hidden !important; ' +
            '}' +
            '.page-content { ' + 'padding: 0 20px !important; ' + '}';

            document.head.appendChild(style);
            var bodyContent = document.body.innerHTML;
            document.body.innerHTML = '<div class="page-content">' + bodyContent + '</div>';
        var totalWidth = document.body.scrollWidth;
        var pageCount = Math.ceil(totalWidth / screenWidth);

        Android.onPageCountCalculated(pageCount, screenWidth);

        window.addEventListener('scroll', function() {
            var newPage = Math.round(window.scrollX / screenWidth);
            Android.onPageChanged(newPage);
            }, { passive: true });

    } catch(e) {
        console.log(e.message);
    }
    })();
    """.trimIndent()

                                view?.evaluateJavascript(js, null)

                                view?.post {
                                    val target = (currentPage * columnWidth * density).toInt()
                                    view.scrollTo(target, 0)
                                }

                            }
                        }

                        webViewRef.value = this
                    }
                },
                update = {  },
                modifier = Modifier.fillMaxSize()
            )

            LaunchedEffect(currentChapter, mapSelectionMode, defSelectionMode) {
                if (mapSelectionMode || defSelectionMode) {
                    currentPage = 0
                }
                webViewRef.value?.loadDataWithBaseURL(
                    null,
                    chapterHtml,
                    "text/html",
                    "UTF-8",
                    null
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { offset ->
                                val screenWidth = size.width
                                val tapX = offset.x
                                when {
                                    tapX < screenWidth / 3 -> goToPreviousPage()
                                    tapX > 2 * screenWidth / 3 -> goToNextPage()
                                }
                            },
                            onDoubleTap = { offset ->
                                togglePanels()
                            }
                        )
                    }
            )
        }
    }
}
