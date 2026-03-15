package com.example.cheatai.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import com.example.cheatai.R
import com.example.cheatai.data.Book
import com.example.cheatai.data.StaticBooksRepository
import com.example.cheatai.ui.theme.Blue
import com.example.cheatai.ui.theme.Gradients
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cheatai.components.BookCoverUpload
import com.example.cheatai.components.BottomInputPanel
import com.example.cheatai.components.UploadedFileRow
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.White

@Composable
fun AddBookScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
                    .background(Gradients.bottomUpPanelGradient)
                    .padding(bottom = 18.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.add_new_book),
                    style = CheatAITypography.headlineMedium,
                    color = White,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                )

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(36.dp)
                        .padding(start = 16.dp)
                        .align(Alignment.BottomStart)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Назад",
                        tint = White,

                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.BottomCenter)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 38.dp, bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                BookCoverUpload(
                    modifier = Modifier.fillMaxSize(),
                    onClick = { println("Загрузка обложки") }
                )
            }

            UploadedFileRow(
                fileName = "Загруженный файл:\nHarryPotterAndFhylosovskyKamen.epub",
                onReloadClick = { println("Повторная загрузка") }
            )
            Spacer(modifier = Modifier.height(11.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(353.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 36.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 25.dp,
                                topEnd = 25.dp
                            )
                        )
                        .background(Gradients.bottomUpPanelGradient)
                )

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 75.dp)
                        .height(64.dp)
                        .background(
                            brush = Gradients.circleButtonGradient,
                            shape = RoundedCornerShape(25.dp)
                        ),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.save_book),
                        style = CheatAITypography.titleMedium,
                        color = White
                    )
                }
                BottomInputPanel()


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddBookScreenPreview() {
    AddBookScreen(
        navController = rememberNavController()
    )
}