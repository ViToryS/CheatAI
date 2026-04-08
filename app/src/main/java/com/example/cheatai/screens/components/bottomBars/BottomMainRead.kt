package com.example.cheatai.screens.components.bottomBars

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cheatai.R
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.LocalAppDimensions
import com.example.cheatai.ui.theme.White

@Composable
fun BottomMainRead(
    navController: NavController?,
    bookId: String,
    source: String,
    locatorJson: String = ""
){
    val dimensions = LocalAppDimensions.current

    Box( modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.bottomBarHeight)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(
                    topStart = dimensions.cornerMedium,
                    topEnd = dimensions.cornerMedium
                ))
                .background(brush = Gradients.bottomUpPanelGradient)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { navController?.navigate("chapters/$bookId") },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(
                        topStart = dimensions.cornerMedium,
                        bottomStart = dimensions.cornerMedium,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp
                    ),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.content),
                        style = CheatAITypography.titleMedium,
                        fontSize = dimensions.textMedium,
                        color = White
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(dimensions.buttonHeight * 0.46f)
                        .background(Color.LightGray)
                        .align(Alignment.CenterVertically)
                )

                Button(
                    onClick = {
                        val route = if (locatorJson.isNotEmpty()) {
                            "book_notes/${bookId}/${source}?locator=${Uri.encode(locatorJson)}"
                        } else {
                            "book_notes/${bookId}/${source}"
                        }
                        navController?.navigate(route)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(
                        topEnd = dimensions.cornerMedium,
                        bottomEnd = dimensions.cornerMedium,
                        topStart = 0.dp,
                        bottomStart = 0.dp
                    ),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.bookmarkings),
                        style = CheatAITypography.titleMedium,
                        fontSize = dimensions.textMedium,
                        color = White
                    )
                }
            }
        }
    }
}