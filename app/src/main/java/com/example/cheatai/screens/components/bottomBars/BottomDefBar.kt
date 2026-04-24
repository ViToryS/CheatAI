package com.example.cheatai.screens.components.bottomBars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cheatai.R
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.LocalAppDimensions
import com.example.cheatai.ui.theme.PinkRead
import com.example.cheatai.ui.theme.White

@Composable
fun BottomDefBar(
    selectedWord: String = "",
    definition: String? = null,
    isLoading: Boolean = false,
    onConfirmClick: (String) -> Unit = {}
) {
    val dimensions = LocalAppDimensions.current
    var showEditDialog by remember { mutableStateOf(false) }
    var tempWord by remember { mutableStateOf(selectedWord) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensions.defBarHeight)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.bottomBarHeight)
                .clip(RoundedCornerShape(
                    topStart = dimensions.cornerMedium,
                    topEnd = dimensions.cornerMedium
                ))
                .background(brush = Gradients.bottomUpPanelGradient)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = dimensions.paddingMedium, end = dimensions.paddingSmall),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Поиск по слову: $selectedWord",
                    style = CheatAITypography.labelMedium,
                    fontSize = dimensions.textMedium,
                    color = White,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        tempWord = selectedWord
                        showEditDialog = true
                    },
                    modifier = Modifier.size(dimensions.iconButtonSize)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "Редактировать слово",
                        tint = White,
                        modifier = Modifier.size(dimensions.iconSize)
                    )
                }

                Spacer(modifier = Modifier.width(dimensions.paddingExtraSmall))
                IconButton(
                    onClick = {
                        if (selectedWord.isNotBlank()) {
                            onConfirmClick(selectedWord)
                        }
                    },
                    modifier = Modifier.size(dimensions.iconButtonSize)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check),
                        contentDescription = "Подтвердить слово",
                        tint = PinkRead,
                        modifier = Modifier.size(dimensions.iconSize)
                    )
                }
            }
        }

        if (showEditDialog) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Редактировать слово") },
                text = {
                    OutlinedTextField(
                        value = tempWord,
                        onValueChange = { tempWord = it },
                        label = { Text("Слово для поиска") },
                        singleLine = true
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (tempWord.isNotBlank()) {
                                onConfirmClick(tempWord)
                            }
                            showEditDialog = false
                        }
                    ) {
                        Text("Поиск")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditDialog = false }) {
                        Text("Отмена")
                    }
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White)
                .padding(dimensions.paddingMedium),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Загрузка определения...",
                            style = CheatAITypography.labelMedium,
                            fontSize = dimensions.textSmall,
                            color = Color.Gray
                        )
                    }
                }
                definition != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = definition,
                            color = Color.Black,
                            style = CheatAITypography.labelMedium,
                            textAlign = TextAlign.Center,
                            fontSize = dimensions.textMedium,
                        )
                    }
                }
                else -> {
                    Text(
                        text = "Выделите слово и нажмите на галочку",
                        style = CheatAITypography.labelMedium,
                        fontSize = dimensions.textSmall,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}