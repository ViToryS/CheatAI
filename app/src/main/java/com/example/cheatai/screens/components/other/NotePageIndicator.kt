package com.example.cheatai.screens.components.other

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.example.cheatai.data.model.Book
import com.example.cheatai.data.model.Note
import com.example.cheatai.ui.theme.AppDimensions
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.GrayText


@Composable
fun NotePageIndicator(
    note: Note,
    book: Book,
    dimensions: AppDimensions
) {
    val pageText = note.location.getDisplayText(book)

    Box(
        modifier = Modifier
            .height(dimensions.noteButtonHeight)
            .padding(end = dimensions.paddingMedium)
            .background(
                brush = Gradients.circleButtonGradient,
                shape = RoundedCornerShape(dimensions.cornerSmall)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = pageText,
            style = CheatAITypography.labelMedium,
            color = GrayText,
            fontSize = dimensions.textSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .padding(horizontal = dimensions.paddingExtraSmall)
        )
    }
}
