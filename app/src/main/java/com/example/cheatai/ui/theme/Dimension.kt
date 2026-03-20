package com.example.cheatai.ui.theme


import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


enum class WindowSizeClass { COMPACT, MEDIUM, EXPANDED }

@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    return when {
        configuration.screenWidthDp < 360 -> WindowSizeClass.COMPACT
        configuration.screenWidthDp < 600 -> WindowSizeClass.MEDIUM
        else -> WindowSizeClass.EXPANDED
    }
}

data class AppDimensions(
    val cornerSmall: Dp,
    val cornerMedium: Dp,
    val cornerLarge: Dp,


    val paddingExtraSmall: Dp,
    val scrollAreaPadding: Dp,
    val paddingMedium: Dp,
    val paddingLarge: Dp,
    val paddingExtraLarge: Dp,

    val paddingSmall: Dp,

    val topBarHeight: Dp,
    val topBarBottomPadding: Dp,
    val bottomBarHeight: Dp,

    val buttonHeight: Dp,
    val buttonHorizontalPadding: Dp,
    val iconButtonSize: Dp,
    val iconSize: Dp,


    val defBarHeight: Dp,
    val topButtonSize: Dp,
    val bottomButtonSize: Dp,
    val bottomButtonOffset: Dp,

    val bookCardAspectRatio: Float,
    val bookCardPadding: Dp,
    val bookImageWidthFraction: Float,

    val descriptionTopBlockHeight: Dp,
    val descriptionImageWidthFraction: Float,
    val descriptionAuthorTitlePadding: Dp,

    val textFieldHeight: Dp,
    val noteTextFieldHeight: Dp,

    val textSmall: TextUnit,
    val textMedium: TextUnit,
    val textLarge: TextUnit,
    val addBookPanelHeight: Dp,

    val noteButtonHeight: Dp,
    val noteButtonWidth: Dp,
    val noteButtonIconSize: Dp,
    val noteButtonOffset: Dp,
    val noteButtonsSpacing: Dp,
    val textFieldLargeHeight: Dp)

val LocalAppDimensions = staticCompositionLocalOf {
    AppDimensions(
        topBarHeight = 90.dp,
        topBarBottomPadding = 18.dp,
        scrollAreaPadding = 8.dp,
        bottomButtonSize = 80.dp,
        bottomButtonOffset = 25.dp,

        paddingMedium = 20.dp,

        topButtonSize = 45.dp,


        cornerMedium = 25.dp,



        cornerSmall = 15.dp,
        cornerLarge = 30.dp,


        buttonHorizontalPadding = 75.dp,


        addBookPanelHeight = 250.dp,


        paddingExtraSmall = 4.dp,
        paddingSmall = 10.dp,

        paddingLarge = 24.dp,
        paddingExtraLarge = 32.dp,

        textFieldLargeHeight = 150.dp,
        bottomBarHeight = 70.dp,
        defBarHeight = 200.dp,
        buttonHeight = 58.dp,

        iconButtonSize = 36.dp,
        iconSize = 24.dp,


        bookCardAspectRatio = 1.1f,
        bookCardPadding = 22.dp,
        bookImageWidthFraction = 0.35f,
        descriptionTopBlockHeight = 536.dp,
        descriptionImageWidthFraction = 0.3f,
        descriptionAuthorTitlePadding = 60.dp,
        textFieldHeight = 56.dp,

        textSmall = 12.sp,
        textMedium = 14.sp,
        textLarge = 16.sp,

        noteTextFieldHeight = 150.dp,
        noteButtonHeight = 36.dp,
        noteButtonWidth = 72.dp,
        noteButtonIconSize = 20.dp,
        noteButtonOffset = 18.dp,
        noteButtonsSpacing = 8.dp
    )
}

@Composable
fun provideAppDimensions(sizeClass: WindowSizeClass): AppDimensions {
    return when (sizeClass) {
        WindowSizeClass.COMPACT -> AppDimensions(
            topBarHeight = 60.dp,
            bottomBarHeight = 50.dp,
            defBarHeight = 150.dp,
            topBarBottomPadding = 12.dp,
            scrollAreaPadding = 4.dp,
            bottomButtonSize = 60.dp,
            bottomButtonOffset = 20.dp,

            paddingMedium = 15.dp,

            topButtonSize = 30.dp,

            cornerSmall = 12.dp,
            cornerMedium = 20.dp,
            cornerLarge = 25.dp,

            buttonHorizontalPadding = 15.dp,

            bookCardAspectRatio = 1.1f,

            paddingSmall = 5.dp,
            paddingExtraSmall = 2.dp,


            paddingLarge = 16.dp,
            paddingExtraLarge = 24.dp,



            textFieldLargeHeight = 100.dp,
            buttonHeight = 48.dp,
            iconButtonSize = 32.dp,
            iconSize = 20.dp,




            addBookPanelHeight = 200.dp,

            bookCardPadding = 16.dp,
            bookImageWidthFraction = 0.4f,
            descriptionTopBlockHeight = 250.dp,
            descriptionImageWidthFraction = 0.5f,
            descriptionAuthorTitlePadding = 40.dp,
            textFieldHeight = 48.dp,


            textSmall = 14.sp,
            textMedium = 16.sp,
            textLarge = 16.sp,

            noteTextFieldHeight = 70.dp,
            noteButtonHeight = 15.dp,
            noteButtonWidth = 35.dp,
            noteButtonIconSize = 10.dp,
            noteButtonOffset = 8.dp,
            noteButtonsSpacing = 8.dp
        )

        WindowSizeClass.MEDIUM -> AppDimensions(
            topBarHeight = 70.dp,
            bottomBarHeight = 60.dp,
            defBarHeight = 180.dp,
            topBarBottomPadding = 18.dp,
            scrollAreaPadding = 8.dp,
            bottomButtonSize = 70.dp,
            bottomButtonOffset = 25.dp,

            paddingMedium = 20.dp,
            topButtonSize = 35.dp,

            cornerSmall = 15.dp,
            cornerMedium = 25.dp,
            cornerLarge = 30.dp,
            paddingExtraSmall = 4.dp,

            paddingSmall = 10.dp,
            paddingLarge = 24.dp,
            paddingExtraLarge = 32.dp,

            buttonHorizontalPadding = 45.dp,

            buttonHeight = 58.dp,


            bookCardAspectRatio = 1.1f,


            iconButtonSize = 36.dp,
            iconSize = 24.dp,
            addBookPanelHeight = 250.dp,


            bookCardPadding = 22.dp,
            bookImageWidthFraction = 0.5f,
            descriptionTopBlockHeight = 350.dp,
            descriptionImageWidthFraction = 0.5f,
            descriptionAuthorTitlePadding = 60.dp,
            textFieldHeight = 56.dp,


            textFieldLargeHeight = 150.dp,
            textSmall = 16.sp,
            textMedium = 18.sp,
            textLarge = 20.sp,

            noteTextFieldHeight = 80.dp,
            noteButtonHeight = 20.dp,
            noteButtonWidth = 45.dp,
            noteButtonIconSize = 12.dp,
            noteButtonOffset = 10.dp,
            noteButtonsSpacing = 10.dp
        )

        WindowSizeClass.EXPANDED -> AppDimensions(
            topBarHeight = 90.dp,
            bottomBarHeight = 80.dp,
            defBarHeight = 240.dp,
            topBarBottomPadding = 24.dp,
            scrollAreaPadding = 12.dp,
            bottomButtonSize = 80.dp,
            bottomButtonOffset = 30.dp,

            topButtonSize = 45.dp,
            paddingMedium = 24.dp,


            cornerSmall = 20.dp,
            cornerMedium = 30.dp,
            cornerLarge = 40.dp,
            paddingExtraSmall = 8.dp,

            paddingSmall = 15.dp,
            paddingLarge = 32.dp,
            paddingExtraLarge = 48.dp,



            buttonHeight = 72.dp,
            buttonHorizontalPadding = 150.dp,

            bookCardAspectRatio = 1.2f,
            addBookPanelHeight = 340.dp,



            iconButtonSize = 48.dp,
            iconSize = 32.dp,


            bookCardPadding = 32.dp,
            bookImageWidthFraction = 0.3f,
            descriptionTopBlockHeight = 650.dp,
            descriptionImageWidthFraction = 0.6f,
            descriptionAuthorTitlePadding = 80.dp,
            textFieldHeight = 72.dp,

            textFieldLargeHeight = 200.dp,
            textSmall = 18.sp,
            textMedium = 20.sp,
            textLarge = 26.sp,

            noteTextFieldHeight = 200.dp,
            noteButtonHeight = 30.dp,
            noteButtonWidth = 65.dp,
            noteButtonIconSize = 18.dp,
            noteButtonOffset = 15.dp,
            noteButtonsSpacing = 12.dp
        )
    }
}