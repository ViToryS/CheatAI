package com.example.cheatai.screens.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cheatai.R
import com.example.cheatai.data.model.Book
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.LightGrey
import com.example.cheatai.ui.theme.LocalAppDimensions
import com.example.cheatai.ui.theme.White
import java.io.File

@Composable
fun BookItem(
    book: Book,
    onReadClick: (String) -> Unit,
    onDescriptionClick: (String) -> Unit
) {
    val dimensions = LocalAppDimensions.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.bookCardPadding, vertical = dimensions.bookCardPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(dimensions.bookCardAspectRatio)
                .clip(RoundedCornerShape(dimensions.cornerLarge))
                .background(brush = Gradients.bookCardGradient),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                BookCoverImage(
                    coverUrl = book.coverUrl,
                    modifier = Modifier
                        .aspectRatio(0.7f)
                        .align(Alignment.Center)
                        .padding(top = dimensions.bookCardPadding / 2)
                        .clip(RoundedCornerShape(dimensions.cornerLarge))
                        .background(Color.LightGray)
                )
            }

            Spacer(modifier = Modifier.height(dimensions.bookCardPadding / 2))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensions.bookCardPadding / 2)
                    .padding(bottom = dimensions.buttonHeight / 2 + dimensions.paddingMedium)
            ) {
                Text(
                    text = book.title,
                    style = CheatAITypography.labelMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = dimensions.textMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensions.bookCardPadding * 2f)
                .height(dimensions.buttonHeight)
                .align(Alignment.BottomCenter)
                .offset(y = dimensions.bookCardPadding)
                .clip(RoundedCornerShape(dimensions.cornerMedium))
                .background(brush = Gradients.circleButtonGradient),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onDescriptionClick(book.id) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(
                    topStart = dimensions.cornerLarge,
                    bottomStart = dimensions.cornerLarge,
                    topEnd = 0.dp,
                    bottomEnd = 0.dp
                ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.book_description),
                    style = CheatAITypography.titleMedium,
                    fontSize = dimensions.textMedium,
                    color = White
                )
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(dimensions.buttonHeight * 0.5f)
                    .background(LightGrey)
                    .align(Alignment.CenterVertically)
            )

            Button(
                onClick = { onReadClick(book.id) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(
                    topEnd = dimensions.cornerLarge,
                    bottomEnd = dimensions.cornerLarge,
                    topStart = 0.dp,
                    bottomStart = 0.dp
                ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.book_read),
                    style = CheatAITypography.titleMedium,
                    fontSize = dimensions.textMedium,
                    color = White,
                )
            }
        }
    }
}

@Composable
fun BookCoverImage(
    coverUrl: String,
    modifier: Modifier = Modifier
) {
    when {
        coverUrl.startsWith("drawable://") -> {
            // Старый формат: drawable ресурс
            val resId = parseDrawableRes(coverUrl)
            androidx.compose.foundation.Image(
                painter = painterResource(id = resId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }
        coverUrl.startsWith("/data/") || coverUrl.startsWith("file:") -> {
            // Путь к файлу
            val file = File(coverUrl)
            if (file.exists()) {
                AsyncImage(
                    model = file,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                )
            } else {
                DefaultCoverImage(modifier)
            }
        }
        coverUrl.startsWith("content://") -> {

            AsyncImage(
                model = coverUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }
        else -> {
            DefaultCoverImage(modifier)
        }
    }
}

@Composable
fun DefaultCoverImage(modifier: Modifier = Modifier) {
    androidx.compose.foundation.Image(
        painter = painterResource(id = R.drawable.harry_potter_cover),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

fun parseDrawableRes(url: String): Int {
    return when (url) {
        "drawable://book_cover_1" -> R.drawable.harry_potter_cover
        else -> R.drawable.harry_potter_cover
    }
}