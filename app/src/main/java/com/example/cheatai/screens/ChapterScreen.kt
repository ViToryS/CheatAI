package com.example.cheatai.screens

import com.example.cheatai.utils.Chapter
import com.example.cheatai.utils.ChaptersParser
import android.util.Log
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.cheatai.R
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.LocalAppDimensions
import com.example.cheatai.ui.theme.PinkRead
import com.example.cheatai.ui.theme.White


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChaptersScreen(
    navController: NavController,
    onChapterClick: (Chapter) -> Unit
) {
    val context = LocalContext.current
    val parser = remember { ChaptersParser(context) }
    val dimensions = LocalAppDimensions.current

    var chapters by remember { mutableStateOf<List<Chapter>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        chapters = parser.loadChaptersFromResource(R.raw.chapters_config)
        chapters.forEachIndexed { index, chapter ->
            Log.d("ChaptersScreen", "Глава $index: ${chapter.title}")
        }
        isLoading = false
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

                chapters.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.no_chapters))
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(
                            items = chapters,
                            key = { it.id }
                        ) { chapter: Chapter ->
                            ChapterItem(
                                chapter = chapter,
                                onClick = { onChapterClick(chapter) }
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
            modifier = Modifier.padding(horizontal = dimensions.paddingMedium,
                vertical = dimensions.paddingMedium)
        )
    }
}