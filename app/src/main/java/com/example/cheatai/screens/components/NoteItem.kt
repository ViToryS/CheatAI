package com.example.cheatai.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.cheatai.R

import com.example.cheatai.data.model.Book
import com.example.cheatai.data.model.Note
import com.example.cheatai.screens.components.buttons.NoteActionButton
import com.example.cheatai.screens.components.other.NotePageIndicator
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.DarkPink
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.GrayText
import com.example.cheatai.ui.theme.LocalAppDimensions
import com.example.cheatai.ui.theme.White
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun NoteItem(
    note: Note,
    book: Book,
    onNoteClick: (Note) -> Unit,
    onEditClick: (Note) -> Unit,
    onDeleteClick: (Note) -> Unit,
    onNavigateClick: (Note) -> Unit
) {
    val dimensions = LocalAppDimensions.current

    var isEditing by remember { mutableStateOf(false) }
    var editedTitle by remember { mutableStateOf(note.title) }
    var editedContent by remember { mutableStateOf(note.content) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.paddingMedium, vertical = dimensions.scrollAreaPadding)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { if (!isEditing) onNoteClick(note) },
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
                        if (isEditing) {
                            BasicTextField(
                                value = editedTitle,
                                onValueChange = { editedTitle = it },
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
                                        innerTextField()
                                    }
                                }
                            )
                        } else {
                            Text(
                                text = note.title,
                                style = CheatAITypography.titleMedium,
                                fontSize = dimensions.textMedium,
                                color = GrayText,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.width(dimensions.paddingExtraSmall))
                        Text(
                            text = formatDate(note.createdAt),
                            style = CheatAITypography.labelSmall,
                            color = GrayText
                        )
                    }
                    Spacer(modifier = Modifier.height(dimensions.paddingExtraSmall))


                    if (isEditing) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = DarkPink.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(dimensions.cornerSmall)
                                )
                                .padding(horizontal = dimensions.paddingSmall)
                                .padding(vertical = dimensions.paddingSmall)
                        ) {
                            BasicTextField(
                                value = editedContent,
                                onValueChange = { editedContent = it },
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
                                        innerTextField()
                                    }
                                }
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = dimensions.noteTextFieldHeight)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = note.content,
                                style = CheatAITypography.labelMedium,
                                color = White,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
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
                NotePageIndicator(
                    note = note,
                    book = book,
                    dimensions = dimensions
                )


                val cardWidth = LocalConfiguration.current.screenWidthDp.dp -
                        (dimensions.paddingMedium * 2)
                val buttonsBlockWidth = cardWidth * 0.4f

                Box(
                    modifier = Modifier.width(buttonsBlockWidth)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NoteActionButton(
                            onClick = { onDeleteClick(note) },
                            icon = R.drawable.ic_delete,
                            contentDescription = stringResource(R.string.remove),
                            dimensions = dimensions,
                            modifier = Modifier
                                .weight(1f)
                                .height(dimensions.noteButtonHeight)
                        )

                        Spacer(modifier = Modifier.width(dimensions.noteButtonsSpacing))

                        NoteActionButton(
                            onClick = {
                                if (isEditing) {

                                    val updatedNote = note.copy(
                                        title = editedTitle,
                                        content = editedContent,
                                        updatedAt = System.currentTimeMillis()
                                    )
                                    onEditClick(updatedNote)
                                    isEditing = false
                                } else {
                                    editedTitle = note.title
                                    editedContent = note.content
                                    isEditing = true
                                }
                            },
                            icon = if (isEditing) R.drawable.ic_check else R.drawable.ic_edit,
                            contentDescription = if (isEditing) stringResource(R.string.save_book)
                            else stringResource(R.string.edit),
                            dimensions = dimensions,
                            modifier = Modifier
                                .weight(1f)
                                .height(dimensions.noteButtonHeight),
                        )



                        Spacer(modifier = Modifier.width(dimensions.noteButtonsSpacing))

                        NoteActionButton(
                            onClick = { onNavigateClick(note) },
                            icon = R.drawable.ic_navigate,
                            contentDescription = stringResource(R.string.go_to),
                            dimensions = dimensions,
                            modifier = Modifier
                                .weight(1f)
                                .height(dimensions.noteButtonHeight)
                        )

                    }
                }
            }
        }}}


fun formatDate(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 3600000 -> "${diff / 60000} мин назад"
        diff < 86400000 -> "${diff / 3600000} ч назад"
        else -> {
            val date = Date(timestamp)
            SimpleDateFormat("dd.MM.yy", Locale.getDefault()).format(date)
        }
    }
}
