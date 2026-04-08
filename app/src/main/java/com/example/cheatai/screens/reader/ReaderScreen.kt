package com.example.cheatai.screens.reader

import android.content.res.Configuration
import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.cheatai.CheatAIApplication
import com.example.cheatai.screens.components.bottomBars.BottomDefBar
import com.example.cheatai.screens.components.bottomBars.BottomMainRead
import com.example.cheatai.screens.components.bottomBars.BottomMapBar
import com.example.cheatai.screens.components.topBars.TopBarRead
import com.example.cheatai.ui.theme.GrayText
import com.example.cheatai.ui.theme.LocalAppDimensions
import com.example.cheatai.ui.theme.PinkRead
import kotlinx.coroutines.launch
import org.readium.r2.navigator.epub.EpubDefaults
import org.readium.r2.navigator.epub.EpubNavigatorFactory
import org.readium.r2.navigator.epub.EpubNavigatorFragment
import org.readium.r2.navigator.epub.EpubPreferences
import org.readium.r2.navigator.preferences.Color
import org.readium.r2.navigator.preferences.FontFamily
import org.readium.r2.shared.ExperimentalReadiumApi
import org.readium.r2.shared.util.AbsoluteUrl

@OptIn(ExperimentalReadiumApi::class)
@Composable
fun ReaderScreen(
    navController: NavController,
    bookId: String,
    initialLocatorJson: String = ""
) {
    val dimensions = LocalAppDimensions.current
    val context = LocalContext.current
    val contentResolver = LocalContext.current.contentResolver

    val viewModel = remember {
        ReaderViewModel(
            CheatAIApplication.repository,
            contentResolver,
            context
        )
    }

    val publication by viewModel.publication.collectAsState()
    val bookEntity by viewModel.bookEntity.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val showTopPanel by viewModel.showTopPanel.collectAsState()
    val showBottomPanel by viewModel.showBottomPanel.collectAsState()
    val mapSelectionMode by viewModel.mapSelectionMode.collectAsState()
    val defSelectionMode by viewModel.defSelectionMode.collectAsState()
    val currentLocator by viewModel.currentLocator.collectAsState()
    val selectedWord by viewModel.selectedWord.collectAsState()
    val selectedPlace by viewModel.selectedPlace.collectAsState()

    var lastTapTime by remember { mutableStateOf(0L) }
    var fragment by remember { mutableStateOf<EpubNavigatorFragment?>(null) }

    val activity = context as FragmentActivity

    LaunchedEffect(bookId) {
        viewModel.loadBook(bookId.toLong())
    }


    val locatorListener = object : EpubNavigatorFragment.Listener {
        private var lastTapTime = 0L
        override fun onTap(point: PointF): Boolean {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTapTime < 500) {
                viewModel.togglePanels()
            }
            lastTapTime = currentTime
            return true
        }

        override fun onExternalLinkActivated(url: AbsoluteUrl) {}
    }


    Box(modifier = Modifier.fillMaxSize()) {
        when {
            errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Ошибка: $errorMessage")
                }
            }

            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Загрузка книги...")
                }
            }

            publication != null && bookEntity != null -> {
                val finalInitialLocator = viewModel.getInitialLocator(
                    initialLocatorJson,
                    bookEntity?.lastLocator
                )




                val defaults = EpubDefaults(
                    scroll = false,
                    pageMargins = 1.5,
                    lineHeight = 1.1,
                    paragraphSpacing = 1.0,
                    publisherStyles = false
                )

                val config = EpubNavigatorFactory.Configuration(defaults = defaults)

                val epubNavigatorFactory = EpubNavigatorFactory(
                    publication = publication!!,
                    configuration = config
                )

                val readiumBackgroundColor = Color(PinkRead.toArgb())
                val readiumTextColor = Color(GrayText.toArgb())

                val preferences = EpubPreferences(
                    backgroundColor = readiumBackgroundColor,
                    textColor = readiumTextColor,
                    fontFamily = FontFamily.SERIF,
                    paragraphIndent = 1.0,
                    lineHeight = 1.2,
                    pageMargins = 1.5,
                    paragraphSpacing = 1.0,
                    publisherStyles = false
                )

                val fragmentFactory = epubNavigatorFactory.createFragmentFactory(
                    initialLocator = finalInitialLocator,
                    initialPreferences = preferences,
                    listener = locatorListener,
                    paginationListener = null,
                    configuration = EpubNavigatorFragment.Configuration()
                )

                activity.supportFragmentManager.fragmentFactory = fragmentFactory

                val containerId = remember { View.generateViewId() }

                Scaffold(
                    topBar = {
                        if (showTopPanel) {
                            TopBarRead(
                                navController = navController,
                                bookId = bookId,
                                mapSelectionMode = mapSelectionMode,
                                defSelectionMode = defSelectionMode,
                                onToggleMapSelection = {
                                    viewModel.toggleMapSelection()
                                },
                                onToggleDefSelection = { viewModel.toggleDefSelection()
                                }
                            )
                        }
                    },
                    bottomBar = {

                        when {
                            defSelectionMode -> BottomDefBar(selectedWord = selectedWord)
                            mapSelectionMode -> BottomMapBar(selectedPlace = selectedPlace)
                            showBottomPanel -> BottomMainRead(
                                navController = navController,
                                bookId = bookId,
                                "reader",
                                locatorJson = currentLocator?.toJSON().toString()
                            )

                        }

                    }
                ) { paddingValues ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(PinkRead)
                            .padding(
                                top = dimensions.paddingMedium,
                                bottom = dimensions.paddingMedium
                            )
                    ) {
                        AndroidView(
                            factory = { ctx ->
                                FragmentContainerView(ctx).apply {
                                    id = containerId
                                }
                            },
                            update = { containerView ->
                                if (containerView.id == containerId && fragment == null) {
                                    val newFragment =
                                        activity.supportFragmentManager.fragmentFactory.instantiate(
                                            context.classLoader,
                                            EpubNavigatorFragment::class.java.name
                                        ) as EpubNavigatorFragment
                                    newFragment.lifecycleScope.launch {
                                        newFragment.currentLocator.collect { locator ->
                                            viewModel.updateCurrentLocator(locator)
                                            viewModel.saveReadingPosition(
                                                bookId.toLong(),
                                                locator.toJSON().toString()
                                            )
                                        }
                                    }

                                    fragment = newFragment
                                    activity.supportFragmentManager.beginTransaction()
                                        .replace(containerId, newFragment)
                                        .commit()
                                    containerView.post {
                                        newFragment.onConfigurationChanged(
                                            Configuration()
                                        )
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .background(PinkRead)
                                .padding(
                                    top = paddingValues.calculateTopPadding(),
                                    bottom = paddingValues.calculateBottomPadding()
                                )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInteropFilter { motionEvent ->
                                when (motionEvent.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        val currentTime = System.currentTimeMillis()
                                        if (currentTime - lastTapTime < 300) {
                                            viewModel.togglePanels()
                                            true
                                        } else {
                                            lastTapTime = currentTime
                                            false
                                        }
                                    }
                                    else -> false
                                }
                            }
                    )
                }
            }
        }
    }
}