package com.example.cheatai.screens.chapters
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cheatai.CheatAIApplication
import com.example.cheatai.R
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.LocalAppDimensions
import com.example.cheatai.ui.theme.PinkRead
import com.example.cheatai.ui.theme.White
import org.readium.r2.shared.publication.Publication


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChaptersScreen(
    navController: NavController,
    bookId: String
) {
    val context = LocalContext.current
    val dimensions = LocalAppDimensions.current
    val contentResolver = LocalContext.current.contentResolver
    val viewModel = remember {
        ChaptersViewModel(
            CheatAIApplication.repository,
            contentResolver,
            context
        )
    }

    val chapters by viewModel.chapters.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()


    var publication by remember { mutableStateOf<Publication?>(null) }

    LaunchedEffect(bookId) {
        viewModel.loadChapters(bookId)
    }


    Scaffold(
        containerColor = PinkRead,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensions.topBarHeight)
                    .clip(RoundedCornerShape(
                        bottomStart = dimensions.cornerMedium,
                        bottomEnd = dimensions.cornerMedium
                    ))
                    .background(Gradients.bottomUpPanelGradient)
            ) {
                Text(
                    text = stringResource(id = R.string.content),
                    style = CheatAITypography.headlineMedium,
                    fontSize = dimensions.textMedium,
                    color = White,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = dimensions.topBarBottomPadding)
                )

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(dimensions.iconButtonSize)
                        .padding(start = dimensions.paddingSmall)
                        .align(Alignment.BottomStart)
                        .padding(bottom = dimensions.topBarBottomPadding)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = stringResource(R.string.back),
                        tint = White,
                        modifier = Modifier.size(dimensions.iconSize)
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Ошибка: $errorMessage")
                    }
                }

                chapters.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Оглавление не найдено")
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        itemsIndexed(chapters) { index, chapter ->
                            ChapterItem(
                                chapter = chapter,
                                onClick = {
                                    viewModel.onChapterClick(bookId, index) { route ->
                                        navController.navigate(route)

                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChapterItem(
    chapter: Chapter,
    onClick: () -> Unit
) {
    val dimensions = LocalAppDimensions.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (chapter.isSubChapter) {
                    Modifier.padding(start = dimensions.paddingLarge)
                } else {
                    Modifier
                }
            )
            .clickable { onClick() }
    ) {
        Text(
            text = "• ${chapter.title.uppercase()}",
            style = CheatAITypography.labelMedium,
            fontSize = dimensions.textMedium,
            modifier = Modifier.padding(
                horizontal = dimensions.paddingMedium,
                vertical = dimensions.paddingMedium
            )
        )
    }
}