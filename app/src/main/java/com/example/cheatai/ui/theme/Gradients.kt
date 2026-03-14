
package com.example.cheatai.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

object Gradients {
    val bottomUpPanelGradient = Brush.linearGradient(
        0.2f to Purple,
        0.99f to DarkPink,
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    val circleButtonGradient = Brush.linearGradient(
        0f to Blue,
        1f to Peach,
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )
    val bookCardGradient = Brush.linearGradient(
        0.3f to LightPink,
        0.6f to LightBlue,
        1f to DarkBlue,
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )
}