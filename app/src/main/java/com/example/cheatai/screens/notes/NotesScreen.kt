package com.example.cheatai.screens.notes

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cheatai.CheatAIApplication
import com.example.cheatai.R
import com.example.cheatai.data.model.Note
import com.example.cheatai.data.model.NoteLocation
import com.example.cheatai.screens.components.NoteItem
import com.example.cheatai.screens.components.cards.NewNoteItem
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.GrayText
import com.example.cheatai.ui.theme.LocalAppDimensions
import com.example.cheatai.ui.theme.White
import org.json.JSONObject
import org.readium.r2.shared.publication.Locator

@Composable
fun NotesScreen(
    navController: NavController,
    bookId: String,
    source: String = "description",
    initialLocatorJson: String = ""
) {
    val dimensions = LocalAppDimensions.current
    val notesViewModel = remember {
        NotesViewModel(
            CheatAIApplication.noteRepository,
            CheatAIApplication.repository
        )
    }

    val isAddingNewNote by notesViewModel.isAddingNewNote.collectAsState()
    val newNoteTitle by notesViewModel.newNoteTitle.collectAsState()
    val newNoteContent by notesViewModel.newNoteContent.collectAsState()
    val notesList by notesViewModel.notesList.collectAsState()
    val refreshTrigger by notesViewModel.refreshTrigger.collectAsState()
    val book by notesViewModel.book.collectAsState()
    val isLoading by notesViewModel.isLoading.collectAsState()
    val errorMessage by notesViewModel.errorMessage.collectAsState()


    LaunchedEffect(bookId) {
        notesViewModel.loadBook(bookId.toLong())
    }

    LaunchedEffect(refreshTrigger, bookId) {
        notesViewModel.loadNotes(bookId)
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Ошибка: $errorMessage", style = CheatAITypography.labelMedium)
        }
        return
    }

    if (book == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = stringResource(R.string.no_book_found), style = CheatAITypography.labelMedium)
        }
        return
    }

    fun createNewNote() {
        val location = when {
            source == "reader" && initialLocatorJson.isNotEmpty() -> {
                val locator = Locator.fromJSON(JSONObject(initialLocatorJson))
                NoteLocation.ByLocator(locator)
            }
            source == "reader" -> {
                NoteLocation.ByPosition(0f)
            }
            else -> {
                NoteLocation.ByPage(-1)
            }
        }

        notesViewModel.createNewNote(
            bookId = bookId,

            title = newNoteTitle,
            content = newNoteContent,
            location = location
        )
    }

    fun cancelAddNote() {
        notesViewModel.cancelAddNote()
    }

    fun deleteNote(note: Note) {
        notesViewModel.deleteNote(note)
    }

    fun updateNote(updatedNote: Note) {
        notesViewModel.updateNote(updatedNote)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.topBarHeight)
                .align(Alignment.TopCenter)
                .clip(
                    RoundedCornerShape(
                        bottomStart = dimensions.cornerMedium,
                        bottomEnd = dimensions.cornerMedium
                    )
                )
                .background(brush = Gradients.bottomUpPanelGradient)
        ) {
            Text(
                text = stringResource(R.string.bookmarkings),
                style = CheatAITypography.headlineMedium,
                color = White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = dimensions.topBarBottomPadding)
                    .fillMaxWidth()
                    .wrapContentWidth()
            )

            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(
                        start = dimensions.paddingMedium,
                        bottom = dimensions.topBarBottomPadding)
                    .size(dimensions.iconButtonSize)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Назад",
                    tint = White,
                    modifier = Modifier.size(dimensions.iconSize)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .padding(top = dimensions.topBarHeight - dimensions.scrollAreaPadding)
                .padding(bottom = dimensions.bottomBarHeight +
                        dimensions.bottomButtonSize / 2 + dimensions.bottomButtonOffset)
                .padding(horizontal = dimensions.paddingMedium)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(dimensions.cornerMedium))
                    .background(
                        color = White,
                        shape = RoundedCornerShape(dimensions.cornerMedium)
                    ),
                verticalArrangement = Arrangement.spacedBy(dimensions.scrollAreaPadding),
                contentPadding = PaddingValues(
                    bottom = dimensions.noteButtonHeight
                )
            ) {
                if (isAddingNewNote) {
                    item {
                        val locationText = notesViewModel.getLocationText(source, initialLocatorJson)
                        NewNoteItem(
                            title = newNoteTitle,
                            onTitleChange = { notesViewModel.updateNewNoteTitle(it) },
                            content = newNoteContent,
                            onContentChange = { notesViewModel.updateNewNoteContent(it) },
                            onSave = { createNewNote() },
                            onCancel = { cancelAddNote() },
                            locationText = locationText,
                            dimensions = dimensions,
                            book = book!!
                        )
                    }
                }
                if (notesList.isEmpty() && !isAddingNewNote) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "У этой книги пока нет заметок",
                                    style = CheatAITypography.labelMedium,
                                    color = GrayText
                                )
                                Spacer(modifier = Modifier.height(dimensions.scrollAreaPadding))
                                Text(
                                    text = "Нажмите + чтобы добавить",
                                    style = CheatAITypography.labelSmall,
                                    color = GrayText
                                )
                            }
                        }
                    }
                } else {
                    items(notesList) { note ->
                        NoteItem(
                            note = note,
                            book = book!!,
                            onNoteClick = { },
                            onEditClick = { updatedNote ->
                                updateNote(updatedNote)
                            },
                            onDeleteClick = { noteToDelete ->
                                deleteNote(noteToDelete)
                            },
                            onNavigateClick = {
                                when (val location = note.location) {
                                    is NoteLocation.ByLocator -> {
                                        val locatorJson = location.locator?.toJSON().toString() ?: ""
                                        navController.navigate("reader/" +
                                                "${book!!.id}?locator=${Uri.encode(locatorJson)}")
                                    }
                                    else -> {
                                        navController.navigate("reader/${book!!.id}")
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.bottomBarHeight)
                .align(Alignment.BottomCenter)
                .clip(
                    RoundedCornerShape(
                        topStart = dimensions.cornerMedium,
                        topEnd = dimensions.cornerMedium
                    )
                )
                .background(brush = Gradients.bottomUpPanelGradient)
        )

        Button(
            onClick = { notesViewModel.startAddingNote() },
            modifier = Modifier
                .size(dimensions.bottomButtonSize)
                .align(Alignment.BottomCenter)
                .offset(y = -dimensions.bottomButtonOffset)
                .background(
                    brush = Gradients.circleButtonGradient,
                    shape = CircleShape
                ),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                tint = Color.Black,
                contentDescription = "Добавить заметку",
                modifier = Modifier.size(dimensions.bottomButtonSize * 0.6f)
            )
        }
    }
}
