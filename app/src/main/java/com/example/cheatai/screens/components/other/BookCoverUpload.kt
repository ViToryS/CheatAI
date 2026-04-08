package com.example.cheatai.screens.components.other
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import com.example.cheatai.R
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.DarkGray
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.LocalAppDimensions

@Composable
fun BookCoverUpload(
    modifier: Modifier = Modifier,
    coverUri: Uri? = null,
    onCoverClick: () -> Unit = {},
    height: Dp,
){
    val dimensions = LocalAppDimensions.current

    if (coverUri != null) {

        Box(
            modifier = modifier
                .clip(RoundedCornerShape(dimensions.cornerMedium))
                .background(brush = Gradients.addBookCoverGradient)
                .clickable { onCoverClick() },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = coverUri,
                contentDescription = "Обложка книги",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    } else {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(dimensions.cornerMedium))
                .background(brush = Gradients.addBookCoverGradient)
                .clickable { onCoverClick() },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_camera),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(height * 0.5f)
                )
                Text(
                    text = stringResource(R.string.upload_cover),
                    style = CheatAITypography.labelMedium,
                    color = DarkGray,
                    textAlign = TextAlign.Center,
                    fontSize = dimensions.textMedium
                )
            }
        }
    }
}