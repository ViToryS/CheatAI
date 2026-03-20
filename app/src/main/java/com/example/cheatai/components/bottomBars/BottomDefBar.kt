package com.example.cheatai.components.bottomBars

import androidx.compose.runtime.Composable
import com.example.cheatai.ui.theme.LocalAppDimensions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.White

@Composable
fun BottomDefBar(
    selectedWord: String = "мантиях"
) {
    val dimensions = LocalAppDimensions.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensions.defBarHeight)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.bottomBarHeight)
                .clip(RoundedCornerShape(
                    topStart = dimensions.cornerMedium,
                    topEnd = dimensions.cornerMedium
                ))
                .background(brush = Gradients.bottomUpPanelGradient),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Поиск по слову: $selectedWord",
                style = CheatAITypography.labelMedium,
                fontSize = dimensions.textMedium,
                color = White,
                modifier = Modifier.padding(start = dimensions.paddingMedium)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White)
                .padding(dimensions.paddingMedium)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            )
            {
                Text("Ма́нтия (от лат. mantum[1 — «плащ») — часть торжественного облачения монарха," +
                        " служителей церкви, магов, некоторых категорий чиновников (в частности, у судей — " +
                        "судейская мантия), а также учёных и преподавателей (академическая одежда)."
                ,
                    color = Color.Black,
                style = CheatAITypography.labelMedium,
                    textAlign = TextAlign.Center,
                    fontSize = dimensions.textMedium,

                )
            }
        }
    }
}