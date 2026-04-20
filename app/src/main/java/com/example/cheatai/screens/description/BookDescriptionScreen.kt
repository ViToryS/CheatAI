package com.example.cheatai.screens.description

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.example.cheatai.CheatAIApplication
import com.example.cheatai.R
import com.example.cheatai.data.database.entities.toDomainBook
import com.example.cheatai.screens.components.bottomBars.BottomMainRead
import com.example.cheatai.screens.components.cards.BookCoverImage
import com.example.cheatai.screens.components.cards.parseDrawableRes
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.GrayText
import com.example.cheatai.ui.theme.LightPink
import com.example.cheatai.ui.theme.LocalAppDimensions
import com.example.cheatai.ui.theme.White

@Composable
fun BookDescriptionScreen(
    bookId: String,
    navController: NavController? = null
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val dimensions = LocalAppDimensions.current

    val viewModel = remember {
        DescriptionViewModel(CheatAIApplication.repository)
    }

    val bookEntity by viewModel.book.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(bookId) {
        viewModel.loadBook(bookId.toLong())
    }

    val book = bookEntity?.toDomainBook()
    if (isLoading || book == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Загрузка...")
        }
        return
    }

    val source = "description"
    val painter = painterResource(id = parseDrawableRes(
        book.coverUrl
    )
    )
    val intrinsicSize = painter.intrinsicSize

    val imageAspectRatio = if (intrinsicSize.width > 0 && intrinsicSize.height > 0) {
        intrinsicSize.width / intrinsicSize.height
    } else {
        0.7f
    }
    val topBlockHeight = dimensions.descriptionTopBlockHeight
    val availableHeight = topBlockHeight -
            dimensions.paddingLarge -
            dimensions.paddingMedium -
            dimensions.paddingMedium -
            dimensions.paddingExtraSmall -
            dimensions.paddingMedium -
            dimensions.buttonHeight

    Box(modifier = Modifier.fillMaxSize().background(White)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.descriptionTopBlockHeight)
                .clip(RoundedCornerShape(
                    bottomStart = dimensions.cornerMedium,
                    bottomEnd = dimensions.cornerMedium
                ))
                .background(Gradients.bookCardGradient)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = dimensions.paddingMedium)
                    .fillMaxWidth()
                    .padding(horizontal = dimensions.paddingExtraLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BookCoverImage(
                    coverUrl = book.coverUrl,
                    modifier = Modifier
                        .height(availableHeight)
                        .aspectRatio(imageAspectRatio)
                        .clip(RoundedCornerShape(dimensions.cornerMedium))
                )
                Spacer(modifier = Modifier.height(dimensions.paddingMedium))

                Text(
                    text = "${stringResource(id = R.string.athor)}: ${book.author}",
                    style = CheatAITypography.labelMedium,
                    color = White,
                    fontSize = dimensions.textMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(dimensions.paddingExtraSmall))

                Text(
                    text = book.title,
                    style = CheatAITypography.labelMedium,
                    fontSize = dimensions.textMedium,
                    color = White,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = dimensions.paddingMedium)
                )
            }

            IconButton(
                onClick = { navController?.popBackStack() },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = dimensions.paddingMedium, bottom = dimensions.paddingLarge)
                    .size(dimensions.iconButtonSize)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = stringResource(id = R.string.back),
                    tint = White,
                    modifier = Modifier.size(dimensions.iconSize)
                )
            }
            IconButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                    .padding(end = dimensions.paddingMedium, top = dimensions.paddingLarge)
                    .align(Alignment.TopEnd)
                    .size(dimensions.iconButtonSize)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Удалить книгу",
                    tint = White,
                    modifier = Modifier.size(dimensions.iconSize)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .offset(y = dimensions.descriptionTopBlockHeight - dimensions.paddingLarge)
        ) {
            Button(
                onClick = {
                    navController?.navigate("reader/$bookId")
                }
                ,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensions.buttonHeight)
                    .padding(horizontal = dimensions.buttonHorizontalPadding)
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(dimensions.cornerMedium))
                    .background(brush = Gradients.circleButtonGradient),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
            ) {
                Text(
                    text = stringResource(id = R.string.book_read),
                    fontSize = dimensions.textMedium,
                    style = CheatAITypography.titleMedium,
                    color = White
                )
            }
        }
        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = dimensions.descriptionTopBlockHeight + dimensions.buttonHeight
                            - dimensions.scrollAreaPadding,
                    bottom = dimensions.bottomBarHeight + dimensions.paddingLarge,
                    start = dimensions.paddingMedium,
                    end = dimensions.paddingMedium
                )
                .clip(RoundedCornerShape(dimensions.cornerSmall))
                .background(LightPink.copy(alpha = 0.18f))
                .verticalScroll(scrollState)
                .padding(dimensions.scrollAreaPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = book.description.ifBlank { "Без описания" },
                style = CheatAITypography.labelMedium,
                fontSize = dimensions.textMedium,
                color = GrayText,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            BottomMainRead(
                navController,
                bookId,
                source
            )
        }
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удалить книгу?") },
            text = { Text("Вы уверены, что хотите удалить книгу \"${book.title}\"? Это действие нельзя отменить.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteBook(bookId.toLong()) {
                            navController?.popBackStack()
                        }
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Удалить", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
}