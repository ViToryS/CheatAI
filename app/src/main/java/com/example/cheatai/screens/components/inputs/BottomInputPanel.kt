package com.example.cheatai.screens.components.inputs
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.cheatai.R
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.LocalAppDimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomInputPanel(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    val dimensions = LocalAppDimensions.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = dimensions.buttonHeight + dimensions.paddingMedium,
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
                value = title,
                onValueChange = onTitleChange,
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
                value = description,
                onValueChange = onDescriptionChange,
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