package com.example.cheatai.screens.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.cheatai.R

import com.example.cheatai.data.model.Book
import com.example.cheatai.screens.components.buttons.NoteActionButton
import com.example.cheatai.ui.theme.AppDimensions
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.DarkPink
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.GrayText


@Composable
fun NewNoteItem(
    title: String,
    onTitleChange: (String) -> Unit,
    content: String,
    onContentChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    locationText: String,
    dimensions: AppDimensions,
    book: Book
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.paddingMedium, vertical = dimensions.scrollAreaPadding)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(dimensions.cornerSmall),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .background(brush = Gradients.bookCardGradient)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensions.paddingMedium,
                            end = dimensions.paddingMedium,
                            top = dimensions.paddingMedium,
                            bottom = dimensions.paddingMedium + dimensions.noteButtonOffset
                        )
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = title,
                            onValueChange = onTitleChange,
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = DarkPink.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(dimensions.cornerSmall)
                                )
                                .padding(dimensions.paddingSmall),
                            textStyle = CheatAITypography.titleMedium.copy(
                                fontSize = dimensions.textMedium,
                                color = Color.Black
                            ),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    if (title.isEmpty()) {
                                        Text(
                                            text = stringResource(R.string.title),
                                            style = CheatAITypography.titleMedium,
                                            color = GrayText.copy(alpha = 0.5f)
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(dimensions.paddingExtraSmall))
                        Text(
                            text = stringResource(R.string.new_book),
                            style = CheatAITypography.labelSmall,
                            color = GrayText
                        )
                    }

                    Spacer(modifier = Modifier.height(dimensions.paddingExtraSmall))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = DarkPink.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(dimensions.cornerSmall)
                            )
                            .padding(dimensions.paddingSmall)
                    ) {
                        BasicTextField(
                            value = content,
                            onValueChange = onContentChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = dimensions.noteTextFieldHeight),
                            textStyle = CheatAITypography.labelMedium.copy(
                                color = GrayText
                            ),
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.TopStart
                                ) {
                                    if (content.isEmpty()) {
                                        Text(
                                            text = stringResource(R.string.book_description),
                                            style = CheatAITypography.labelMedium,
                                            color = GrayText.copy(alpha = 0.5f)
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .offset(y = dimensions.noteButtonOffset)
                .padding(horizontal = dimensions.paddingMedium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(dimensions.noteButtonHeight)
                        .background(
                            brush = Gradients.circleButtonGradient,
                            shape = RoundedCornerShape(dimensions.cornerSmall)
                        )
                        .padding(horizontal = dimensions.paddingSmall),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = locationText,
                        style = CheatAITypography.labelMedium,
                        color = GrayText,
                        fontSize = dimensions.textSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                val cardWidth = LocalConfiguration.current.screenWidthDp.dp -
                        (dimensions.paddingMedium * 2)
                val buttonsBlockWidth = cardWidth * 0.3f

                Box(
                    modifier = Modifier.width(buttonsBlockWidth)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        NoteActionButton(
                            onClick = onCancel,
                            icon = R.drawable.ic_close,
                            contentDescription = stringResource(R.string.cancel),
                            dimensions = dimensions,
                            modifier = Modifier
                                .weight(1f)
                                .height(dimensions.noteButtonHeight)
                        )

                        Spacer(modifier = Modifier.width(dimensions.noteButtonsSpacing))

                        NoteActionButton(
                            onClick = onSave,
                            icon = R.drawable.ic_check,
                            contentDescription = stringResource(R.string.save_book),
                            dimensions = dimensions,
                            modifier = Modifier
                                .weight(1f)
                                .height(dimensions.noteButtonHeight)
                        )
                    }
                }
            }
        }
    }
}
