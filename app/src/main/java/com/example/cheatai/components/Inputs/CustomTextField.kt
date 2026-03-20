package com.example.cheatai.components.Inputs

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.example.cheatai.ui.theme.LocalAppDimensions
import com.example.cheatai.ui.theme.TintGray

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: Int,
    modifier: Modifier = Modifier,
    iconSize: Dp,
    textStyle: TextStyle,
    cornerRadius: Dp,
    fontSize: TextUnit,
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    var isFocused by remember { mutableStateOf(false) }
    val dimensions = LocalAppDimensions.current
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color.White)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { isFocused = true }
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensions.paddingMedium),
            textStyle = textStyle.copy(
                color = Color.Black,
                fontSize = fontSize
            ),
            singleLine = singleLine,
            maxLines = maxLines,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = if (value.isEmpty() && !isFocused)
                            Alignment.Center else Alignment.CenterStart
                    ) {
                        if (value.isEmpty() && !isFocused) {
                            Text(
                                text = placeholder,
                                style = textStyle,
                                color = TintGray,
                                fontSize = fontSize,
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            innerTextField()
                        }
                    }

                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        )
    }
}