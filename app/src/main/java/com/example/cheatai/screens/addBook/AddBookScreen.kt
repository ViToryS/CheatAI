package com.example.cheatai.screens.addBook

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cheatai.CheatAIApplication
import com.example.cheatai.R
import com.example.cheatai.screens.components.inputs.BottomInputPanel
import com.example.cheatai.screens.components.other.BookCoverUpload
import com.example.cheatai.screens.components.other.UploadedFileRow
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.LocalAppDimensions
import com.example.cheatai.ui.theme.White
import com.example.cheatai.utils.FileHelper

@Composable
fun AddBookScreen(navController: NavController) {
    val dimensions = LocalAppDimensions.current
    val configuration = LocalConfiguration.current

    val context = LocalContext.current
    val contentResolver = LocalContext.current.contentResolver

    val addBookViewModel = remember {
        AddBookViewModel(
            CheatAIApplication.repository,
            contentResolver,
            context
        )
    }

    val title by addBookViewModel.title.collectAsState()
    val description by addBookViewModel.description.collectAsState()
    val fileName by addBookViewModel.fileName.collectAsState()
    val isLoadingMetadata by addBookViewModel.isLoadingMetadata.collectAsState()


    val coverUri by addBookViewModel.coverUri.collectAsState()


    val coverLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            addBookViewModel.updateCoverUri(it)

        }
    }

    val availableHeight = configuration.screenHeightDp.dp -
            dimensions.topBarHeight -
            dimensions.addBookPanelHeight -
            dimensions.paddingMedium * 4 -
            dimensions.iconButtonSize -
            dimensions.paddingExtraSmall

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            addBookViewModel.updateSelectedFileUri(it)
            addBookViewModel.updateFileName(FileHelper.getFileName(it, contentResolver))
            addBookViewModel.parseEpubMetadata(it)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensions.topBarHeight)
                    .clip(RoundedCornerShape(
                        bottomStart = dimensions.cornerMedium,
                        bottomEnd = dimensions.cornerMedium
                    ))
                    .background(Gradients.bottomUpPanelGradient)
            ) {
                Text(
                    text = stringResource(id = R.string.add_new_book),
                    style = CheatAITypography.headlineMedium,
                    fontSize = dimensions.textMedium,
                    color = White,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = dimensions.topBarBottomPadding)
                )

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(dimensions.iconButtonSize)
                        .padding(start = dimensions.paddingSmall)
                        .align(Alignment.BottomStart)
                        .padding(bottom = dimensions.topBarBottomPadding)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = stringResource(R.string.back),
                        tint = White,
                        modifier = Modifier.size(dimensions.iconSize)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(
                        top = dimensions.paddingMedium,
                        bottom = dimensions.paddingMedium
                    ),
                contentAlignment = Alignment.Center
            ) {
                BookCoverUpload(
                    modifier = Modifier
                        .height(availableHeight)
                        .width(availableHeight * 0.7f),
                    coverUri = coverUri,
                    onCoverClick = {

                        coverLauncher.launch("image/*")
                    },
                    height = availableHeight * 0.7f
                )
            }

            UploadedFileRow(
                fileName = fileName?.let { "Загруженный файл:\n$it" } ?: "Файл не выбран",
                onReloadClick = { launcher.launch("application/epub+zip") }
            )
            Spacer(modifier = Modifier.height(dimensions.paddingSmall))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensions.addBookPanelHeight + dimensions.buttonHeight/2)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = dimensions.paddingLarge)
                        .clip(
                            RoundedCornerShape(
                                topStart = dimensions.cornerMedium,
                                topEnd = dimensions.cornerMedium
                            )
                        )
                        .background(Gradients.bottomUpPanelGradient)
                )

                Button(
                    onClick = { addBookViewModel.saveBook {
                        navController.popBackStack()  }
                              },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .padding(horizontal = dimensions.buttonHorizontalPadding)
                        .height(dimensions.buttonHeight)
                        .clip(RoundedCornerShape(dimensions.cornerMedium))
                        .background(
                            brush = Gradients.circleButtonGradient,
                            shape = RoundedCornerShape(dimensions.cornerMedium)
                        ),
                    shape = RoundedCornerShape(dimensions.cornerMedium),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.save_book),
                        style = CheatAITypography.titleMedium,
                        color = White,
                        fontSize = dimensions.textMedium
                    )
                }

                Spacer(modifier = Modifier.height(dimensions.paddingMedium))
                BottomInputPanel(
                    title = title,
                    onTitleChange = { addBookViewModel.updateTitle(it) },
                    description = description,
                    onDescriptionChange = { addBookViewModel.updateDescription(it) }
                )

            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoadingMetadata) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }}
    }
}
