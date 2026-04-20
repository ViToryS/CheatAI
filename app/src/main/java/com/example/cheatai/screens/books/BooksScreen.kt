package com.example.cheatai.screens.books

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cheatai.CheatAIApplication
import com.example.cheatai.R
import com.example.cheatai.screens.components.cards.BookItem
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.GrayText
import com.example.cheatai.ui.theme.LocalAppDimensions
import com.example.cheatai.ui.theme.White

@Composable
fun BooksScreen(
    navController: NavController
) {
    val viewModel = remember {
        BooksViewModel(CheatAIApplication.repository)
    }

    val books by viewModel.domainBooks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val dimensions = LocalAppDimensions.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.topBarHeight)
                .align(Alignment.TopCenter)
                .clip(
                    RoundedCornerShape(
                        bottomStart = dimensions.cornerMedium,
                        bottomEnd = dimensions.cornerMedium
                    )
                )
                .background(brush = Gradients.bottomUpPanelGradient)
        ) {
            Text(
                text = stringResource(id = R.string.loaded_books),
                style = CheatAITypography.headlineMedium,
                fontSize = dimensions.textMedium,
                color = White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = dimensions.topBarBottomPadding)
                    .fillMaxWidth()
                    .wrapContentWidth()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .padding(top = dimensions.topBarHeight - dimensions.scrollAreaPadding)
                .padding(bottom = dimensions.bottomBarHeight +
                        dimensions.bottomButtonSize / 2 + dimensions.bottomButtonOffset)
                .padding(horizontal = dimensions.paddingMedium)
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                books.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "У вас пока нет книг",
                                style = CheatAITypography.labelMedium,
                                color = GrayText
                            )
                            Spacer(modifier = Modifier.height(dimensions.scrollAreaPadding))
                            Text(
                                text = "Нажмите + чтобы добавить книгу",
                                style = CheatAITypography.labelSmall,
                                color = GrayText
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(dimensions.cornerMedium))
                            .background(
                                color = White,
                                shape = RoundedCornerShape(dimensions.cornerMedium)
                            ),
                        verticalArrangement = Arrangement.spacedBy(dimensions.scrollAreaPadding)
                    ) {
                        items(books) { book ->
                            BookItem(
                                book = book,
                                onDescriptionClick = { bookId ->
                                    navController.navigate("book_description/$bookId")
                                },
                                onReadClick = { bookId ->
                                    navController.navigate("reader/$bookId")
                                }
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.bottomBarHeight)
                .align(Alignment.BottomCenter)
                .clip(
                    RoundedCornerShape(
                        topStart = dimensions.cornerMedium,
                        topEnd = dimensions.cornerMedium
                    )
                )
                .background(brush = Gradients.bottomUpPanelGradient)
        )

        Button(
            onClick = {
                navController.navigate("add_screen")
            },
            modifier = Modifier
                .size(dimensions.bottomButtonSize)
                .align(Alignment.BottomCenter)
                .offset(y = -dimensions.bottomButtonOffset)
                .background(
                    brush = Gradients.circleButtonGradient,
                    shape = CircleShape
                ),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                tint = Color.Black,
                contentDescription = stringResource(id = R.string.button_add),
                modifier = Modifier.size(dimensions.bottomButtonSize * 0.55f)
            )
        }
    }
}
