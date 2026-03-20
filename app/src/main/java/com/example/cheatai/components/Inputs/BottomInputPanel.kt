package com.example.cheatai.components.Inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import com.example.cheatai.R

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.res.stringResource
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.LocalAppDimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomInputPanel() {
    val dimensions = LocalAppDimensions.current

    var titleText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = dimensions.buttonHeight+dimensions.paddingMedium,
                start = dimensions.paddingMedium,
                end = dimensions.paddingMedium
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensions.paddingMedium)
        ) {
            CustomTextField(
                value = titleText,
                onValueChange = { titleText = it },
                placeholder = stringResource(R.string.book_name),
                icon = R.drawable.ic_book,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                iconSize = dimensions.iconSize,
                textStyle = CheatAITypography.labelMedium,
                cornerRadius = dimensions.cornerMedium,
                fontSize = dimensions.textMedium
            )

            CustomTextField(
                value = descriptionText,
                onValueChange = { descriptionText = it },
                placeholder = stringResource(R.string.book_description),
                icon = R.drawable.ic_description,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                iconSize = dimensions.iconSize,
                textStyle = CheatAITypography.labelMedium,
                cornerRadius = dimensions.cornerMedium,
                singleLine = false,
                maxLines = 5,
                fontSize = dimensions.textMedium
            )
        }
        Spacer(modifier = Modifier.height(dimensions.paddingMedium))
    }
}