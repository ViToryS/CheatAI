package com.example.cheatai.components.other

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.cheatai.R
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.DarkGray
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.LocalAppDimensions
import com.example.cheatai.ui.theme.White

@Composable
fun UploadedFileRow(
    fileName: String,
    onReloadClick: () -> Unit,

    ) {
    val dimensions = LocalAppDimensions.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.paddingMedium)
            .padding(bottom = dimensions.paddingSmall)
            .background(color = White.copy()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = fileName,
            style = CheatAITypography.labelMedium,
            color = DarkGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = dimensions.textMedium,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(dimensions.paddingSmall))

        IconButton(
            onClick = onReloadClick,
            modifier = Modifier
                .size(dimensions.iconButtonSize)
                .background(
                    brush = Gradients.circleButtonGradient,
                    shape = CircleShape
                )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_reload),
                contentDescription = "Загрузить заново",
                tint = Color.Black,
                modifier = Modifier.size(dimensions.iconSize)
            )
        }
    }
}