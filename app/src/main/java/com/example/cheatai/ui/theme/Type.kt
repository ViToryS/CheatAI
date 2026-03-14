package com.example.cheatai.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.cheatai.R


val EnoFamilyFont = FontFamily(
    Font(
        resId = R.font.eno_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.eno_bold,
        weight = FontWeight.Bold
    )

)

val firaSansCondensedFontFamily = FontFamily(
    Font(
        resId = R.font.firasanscondensed_bold,
        weight = FontWeight.Bold
    )
)
val CheatAITypography = Typography(
    labelMedium = TextStyle(
        fontFamily =  EnoFamilyFont,
        fontSize = 20.sp
),
    titleMedium = TextStyle(
        fontFamily = EnoFamilyFont,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = firaSansCondensedFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
)