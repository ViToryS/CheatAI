package com.example.cheatai.screens.components.bottomBars


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cheatai.R
import com.example.cheatai.screens.components.maps.YandexMapView
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.LocalAppDimensions
import com.example.cheatai.ui.theme.White

@Composable
fun BottomMapBar(
    selectedPlace: String = "Лондон"
) {
    val dimensions = LocalAppDimensions.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    var searchQuery by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(selectedPlace) {
        if (selectedPlace.isNotBlank() && selectedPlace != "Лондон") {
            searchQuery = selectedPlace
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight / 2)
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
                text = "${stringResource(R.string.find_by_map)}: $selectedPlace",
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
        ) {
            YandexMapView(
                searchQuery = searchQuery,
                onPlaceFound = { point, placeName ->
                    if (point != null && placeName != null) {
                        println("Место найдено: $placeName")
                    } else {
                        println("Место не найдено: $selectedPlace")
                    }
                }
            )
        }
    }
}