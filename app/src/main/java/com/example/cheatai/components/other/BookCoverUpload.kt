package com.example.cheatai.components.other
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.example.cheatai.R
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.DarkGray
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.LocalAppDimensions


@Composable
fun BookCoverUpload(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    height: Dp,
){
    val dimensions = LocalAppDimensions.current
    val textStyle = CheatAITypography.labelMedium


    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensions.cornerMedium))
            .background(brush = Gradients.addBookCoverGradient),
        // .clickable(
        //     interactionSource = remember { MutableInteractionSource() },
        //     indication = null
        // ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .wrapContentHeight()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(height * 0.5f)
            )

            Text(
                text = stringResource(R.string.upload_cover),
                style = textStyle,
                color = DarkGray,
                textAlign = TextAlign.Center,
                fontSize = dimensions.textMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }}