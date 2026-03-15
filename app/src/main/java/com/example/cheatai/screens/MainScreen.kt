package com.example.cheatai.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cheatai.R
import com.example.cheatai.data.Book
import com.example.cheatai.data.StaticBooksRepository
import com.example.cheatai.ui.theme.Gradients
import androidx.compose.foundation.lazy.items
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cheatai.components.BookItem
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.White


@Composable

fun MainScreen(
    navController: NavController,
    books: List<Book> = remember { StaticBooksRepository().getBooks() }
) {
    var clickCount by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .align(Alignment.TopCenter)
                .clip(
                    RoundedCornerShape(
                        bottomStart = 25.dp,
                        bottomEnd = 25.dp
                    )
                )
                .background(
                    brush = Gradients.bottomUpPanelGradient
                )
        ) {
            Text(
                text = stringResource(id = R.string.loaded_books),
                style = CheatAITypography.headlineMedium,
                color = White,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
                    .wrapContentWidth()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
                .padding(bottom = 110.dp)
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(
                    color = White,
                    shape = RoundedCornerShape(25.dp)
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(books) { book ->
                    BookItem(
                        book = book,
                        onDescriptionClick = { bookId ->
                            navController.navigate("book_description/$bookId")
                        },
                        onReadClick = { bookId ->
                            println("Читаем книгу $bookId")
                        }
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(77.dp)
                .align(Alignment.BottomCenter)
                .clip(
                    RoundedCornerShape(
                        topStart = 25.dp,
                        topEnd = 25.dp
                    )
                )
                .background(
                    brush = Gradients.bottomUpPanelGradient
                )
        )
        Button(
            onClick = {
                navController.navigate("add_screen")
            },
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.BottomCenter)
                .offset(y = (-25).dp)
                .background(
                    brush = Gradients.circleButtonGradient,
                    shape = CircleShape
                )
                ,
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
                modifier = Modifier.size(49.dp)
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        navController = rememberNavController()
    )
}