package com.example.cheatai.screens

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cheatai.R
import com.example.cheatai.components.NoteItem
import com.example.cheatai.components.cards.NewNoteItem
import com.example.cheatai.data.Note
import com.example.cheatai.data.NoteLocation
import com.example.cheatai.data.NotesRepository
import com.example.cheatai.data.StaticBooksRepository
import com.example.cheatai.ui.theme.CheatAITypography
import com.example.cheatai.ui.theme.Gradients
import com.example.cheatai.ui.theme.GrayText
import com.example.cheatai.ui.theme.LocalAppDimensions
import com.example.cheatai.ui.theme.White

@Composable
fun NotesScreen(
    navController: NavController,
    bookId: String,
    source: String = "description"
) {
    val dimensions = LocalAppDimensions.current
    val repository = remember { NotesRepository() }
    val booksRepository = remember { StaticBooksRepository() }

    var refreshTrigger by remember { mutableStateOf(false) }
    var isAddingNewNote by remember { mutableStateOf(false) }
    var newNoteTitle by remember { mutableStateOf("") }
    var newNoteContent by remember { mutableStateOf("") }

    val book = remember(bookId) { booksRepository.getBooks().find { it.id == bookId } }

    if (book == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = stringResource(R.string.no_book_found), style = CheatAITypography.labelMedium)
        }
        return
    }

    val notes = remember(refreshTrigger) { repository.getNotesForBook(bookId) }


    fun createNewNote() {
        val location = when {
            source == "reader" -> {
                val position = 0.35f
                NoteLocation.ByPosition(position)
            }
            else -> {
                NoteLocation.ByPage(-1)
            }
        }

        val newNote = Note(
            title = newNoteTitle,
            content = newNoteContent,
            bookId = bookId,
            location = location
        )

        repository.addNote(newNote)
        refreshTrigger = !refreshTrigger
        isAddingNewNote = false
        newNoteTitle = ""
        newNoteContent = ""
    }

    fun cancelAddNote() {
        isAddingNewNote = false
        newNoteTitle = ""
        newNoteContent = ""
    }

    fun deleteNote(note: Note) {
        repository.deleteNote(note.id)
        refreshTrigger = !refreshTrigger
    }

    fun updateNote(updatedNote: Note) {
        repository.updateNote(updatedNote)
        refreshTrigger = !refreshTrigger
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
                .padding(bottom = dimensions.bottomBarHeight + dimensions.bottomButtonSize / 2 + dimensions.bottomButtonOffset)
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
                        NewNoteItem(
                            title = newNoteTitle,
                            onTitleChange = { newNoteTitle = it },
                            content = newNoteContent,
                            onContentChange = { newNoteContent = it },
                            onSave = { createNewNote() },
                            onCancel = { cancelAddNote() },
                            locationText = if (source.startsWith("reader")) {
                                val position = 0.35
                                val percent = (position * 100)
                                "Поз. $percent%"
                            } else {
                                "О книге..."
                            },
                            dimensions = dimensions,
                            book = book
                        )
                    }
                }
                if (notes.isEmpty() && !isAddingNewNote) {
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
                    items(notes) { note ->
                        NoteItem(
                            note = note,
                            book = book,
                            onNoteClick = { },
                            onEditClick = { updatedNote ->
                                updateNote(updatedNote)
                            },
                            onDeleteClick = { noteToDelete ->
                                deleteNote(noteToDelete)
                            },
                            onNavigateClick = {
                                navController.navigate("reader/${book.id}/${note.location}")
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
            onClick = {
                isAddingNewNote = true
            },
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
