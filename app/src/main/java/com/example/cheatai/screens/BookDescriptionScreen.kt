package com.example.cheatai.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cheatai.R
import com.example.cheatai.data.StaticBooksRepository
import com.example.cheatai.ui.theme.Gradients
import androidx.compose.material3.IconButton
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.White

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.cheatai.components.parseDrawableRes

import com.example.cheatai.ui.theme.GrayText
import com.example.cheatai.ui.theme.LightPink


@Composable
fun BookDescriptionScreen(
    bookId: String,
    navController: NavController? = null
) {
    val book = remember { StaticBooksRepository().getBooks().find { it.id == bookId } }

    if (book == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Книга не найдена", style = CheatAITypography.labelMedium)
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(White)) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(536.dp)
                .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
                .background(Gradients.bookCardGradient)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 37.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = parseDrawableRes(book.coverUrl)),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .clip(RoundedCornerShape(25.dp)),
                    contentScale = ContentScale.Crop,
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Автор: ${book.author}",
                    style = CheatAITypography.labelMedium,
                    color = White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = book.title,
                    style = CheatAITypography.labelMedium,
                    color = White,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(
                onClick = { navController?.popBackStack() },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 37.dp)
                    .size(36.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Назад",
                    tint = White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .offset(y = 509.dp)
        ) {
            Button(
                onClick = { println("Читаем книгу ${book.id}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .padding(horizontal = 75.dp)
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(25.dp))
                    .background(brush = Gradients.circleButtonGradient),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
            ) {
                Text(
                    text = stringResource(id = R.string.book_read),
                    style = CheatAITypography.titleMedium,
                    color = White
                )
            }
        }

        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 580.dp, bottom = 101.dp, start = 20.dp, end = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(LightPink.copy(alpha = 0.18f))
                .verticalScroll(scrollState)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = book.description,
                style = CheatAITypography.labelMedium,
                color = GrayText,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(77.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .background(brush = Gradients.bottomUpPanelGradient)
        ){
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { println("Содержание ${book.id}") },
                modifier = Modifier.weight(1f).fillMaxHeight(),
                shape = RoundedCornerShape(
                    topStart = 25.dp,
                    bottomStart = 25.dp,
                    topEnd = 0.dp,
                    bottomEnd = 0.dp
                ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text= stringResource(id = R.string.content),
                    style = CheatAITypography.titleMedium,
                    color = White
                )
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(38.dp)
                    .background(Color.LightGray)
                    .align(Alignment.CenterVertically)
            )

            Button(
                onClick = { println("Закладки ${book.id}") },
                modifier = Modifier.weight(1f).fillMaxHeight(),
                shape = RoundedCornerShape(
                    topEnd = 25.dp,
                    bottomEnd = 25.dp,
                    topStart = 0.dp,
                    bottomStart = 0.dp
                ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.bookmarkings),
                    style = CheatAITypography.titleMedium,
                    color = White
                )
            }
        }
    }}
}


@Preview(showBackground = true)
@Composable
fun BookDescriptionScreenPreview() {
    BookDescriptionScreen(bookId = "1", navController = null)
}