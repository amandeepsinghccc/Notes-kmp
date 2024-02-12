package org.co.notes.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import navigation.components.EditScreenComponent
import org.co.notes.MainViewModel
import org.co.notes.NotesModel
import org.co.notes.StateUi
import org.co.notes.domain.DateTimeUtil
import org.co.notes.font
import theme.BgColor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EditScreen(viewModel: MainViewModel, stateUi: StateUi, component: EditScreenComponent) {
    val nunito =
        FontFamily(font("Nunito", "nunito_regular.ttf", FontWeight.Normal, FontStyle.Normal))
    var notesModel by remember {
        mutableStateOf(component.notesModel)
    }
    var title by remember {
        mutableStateOf(notesModel.title)
    }
    var isInEditMode by remember {
        mutableStateOf(notesModel.id == null)
    }
    var body by remember {
        mutableStateOf(notesModel.body)
    }
    var showColorPalette by remember {
        mutableStateOf(false)
    }
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }
    var selectedColorIndex by remember {
        mutableIntStateOf(-1)
    }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboard = LocalSoftwareKeyboardController.current
    Box {
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    snackbar = {
                        Snackbar(
                            snackbarData = it,
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    })
            },
            topBar = {
                TopAppBar(
                    modifier = Modifier.padding(top = 20.dp),
                    title = {
                        WrappedIcon(Icons.Default.ArrowBack) {
                            component.goBack()
                            viewModel.setSelectedNote(null)
                        }
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = Color(
                            0xFF252525
                        )
                    ),
                    actions = {
                        Row(
                            modifier = Modifier.padding(end = 15.dp),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Box {
                                WrappedIcon(if (isInEditMode) "color_palette.xml" else "delete_icon.xml") {
                                    if (!isInEditMode) {
                                        showDeleteDialog = !showDeleteDialog
                                    } else {
                                        showColorPalette = !showColorPalette
                                    }
                                }
                                DropdownMenu(
                                    modifier = Modifier.background(color = BgColor)
                                        .width(40.dp),
                                    properties = PopupProperties(),
                                    expanded = showColorPalette,
                                    onDismissRequest = { showColorPalette = !showColorPalette }) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        NotesModel.colors.forEachIndexed { index, it ->
                                            DropdownMenuItem(
                                                text = {
//                                            Box(modifier = Modifier.size(32.dp).background(it))
                                                },
                                                modifier = Modifier.background(
                                                    color = Color(
                                                        0xFF252525
                                                    )
                                                )
                                                    .size(32.dp)
                                                    .background(color = Color(it))
                                                    .border(
                                                        width = 3.dp,
                                                        color = if (selectedColorIndex == index) Color.Yellow else Color.Transparent
                                                    ),
                                                onClick = {
                                                    showColorPalette = !showColorPalette
                                                    selectedColorIndex = index
                                                }
                                            )
                                            if (index != NotesModel.colors.size - 1) {
                                                Spacer(modifier = Modifier.height(5.dp))
                                            }
                                        }
                                    }
                                }
                            }

                            WrappedIcon(if (isInEditMode) Icons.Default.Done else Icons.Default.Edit) {
                                keyboard?.hide()
                                if (title.isNotEmpty() && title.isNotBlank()) {
                                    if (!isInEditMode) {
                                        isInEditMode = true
                                    } else {
                                        viewModel.createNote(
                                            notesModel.copy(
                                                title = title.trim(),
                                                body = body.trim(),
                                                updatedDate = 1,
                                                colorHex = if (selectedColorIndex != -1) NotesModel.colors[selectedColorIndex] else NotesModel.generateRandomColor()
                                            )
                                        ) {
                                            if (it != (-1).toLong()) {
                                                scope.launch {
                                                    snackbarHostState.showSnackbar(
                                                        message = "Note saved successfully",
                                                        duration = SnackbarDuration.Short
                                                    )
                                                }
                                                isInEditMode = false
                                                notesModel = notesModel.copy(
                                                    id = it
                                                )
                                                keyboard?.hide()
                                            } else {
                                                scope.launch {
                                                    snackbarHostState.showSnackbar(
                                                        message = "Error saving note",
                                                        duration = SnackbarDuration.Short
                                                    )
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Title cannot be empty",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
            },
            containerColor = Color(0xFF252525)
        ) {
            Column(modifier = Modifier.padding(it)) {
                TextField(
                    enabled = isInEditMode,
                    value = title,
                    onValueChange = { inputText ->
                        if (inputText.length <= 70) {
                            title = inputText
                        }
                    },
                    placeholder = {
                        Text(
                            "Title",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = nunito,
                                fontSize = 30.sp,
                                color = Color.LightGray.copy(.5f),
                                fontWeight = FontWeight(550)
                            )
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = nunito,
                        fontSize = 30.sp,
                        color = Color.White,
                        lineHeight = 35.sp,
                        fontWeight = FontWeight(550)
                    )
                )
                if (false) {
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(end = 15.dp),
                        text = "${title.length}/70",
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = nunito,
                            fontSize = 12.sp,
                            color = Color.LightGray.copy(alpha = .5f),
                            fontWeight = FontWeight(500)
                        )
                    )
                }
                Box(modifier=Modifier.weight(1f).verticalScroll(rememberScrollState())){
                    TextField(
                        modifier = Modifier,
                        enabled = isInEditMode,
                        value = body,
                        onValueChange = { inputText ->
                            body = inputText
                        },
                        placeholder = {
                            Text(
                                "Type something...",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = nunito,
                                    fontSize = 20.sp,
                                    color = Color.LightGray.copy(alpha = .5f),
                                    fontWeight = FontWeight(380)
                                )
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = nunito,
                            fontSize = 20.sp,
                            color = Color.White,
                            lineHeight = 23.sp,
                            fontWeight = FontWeight(380)
                        )
                    )
                }
                if (!isInEditMode) {
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(end = 10.dp, bottom = 10.dp),
                        text = "Last updated : ${
                            DateTimeUtil.formatNoteDate(
                                Instant.fromEpochMilliseconds(
                                    notesModel.updatedDate
                                ).toLocalDateTime(TimeZone.currentSystemDefault())
                            )
                        }",
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = nunito,
                            fontSize = 12.sp,
                            color = Color.LightGray.copy(alpha = .8f),
                            fontWeight = FontWeight(500)
                        )
                    )
                }

            }
        }
        if (showDeleteDialog) {
            AlertDialog(
                properties = DialogProperties(usePlatformDefaultWidth = false),
                onDismissRequest = { showDeleteDialog = !showDeleteDialog }) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(.75f),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color(0xff202020)),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Delete Note",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = nunito,
                                fontSize = 22.sp,
                                color = Color.White,
                                lineHeight = 25.sp,
                                fontWeight = FontWeight(600)
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Are you sure you want to delete this note?",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = nunito,
                                fontSize = 17.sp,
                                color = Color.White,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight(450),
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.padding(horizontal = 15.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = .3.dp,
                            color = Color.LightGray
                        )
                        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)) {
                            Text(
                                text = "Delete",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = nunito,
                                    fontSize = 20.sp,
                                    color = Color.Red,
                                    lineHeight = 23.sp,
                                    fontWeight = FontWeight(580),
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier.weight(1f).padding(vertical = 12.dp).clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null
                                ) {
                                    viewModel.deleteNoteById(notesModel.id ?: -1) {
                                        component.goBack()
                                    }
                                    showDeleteDialog = !showDeleteDialog
                                }
                            )
                            Divider(
                                modifier = Modifier.fillMaxHeight().width(.3.dp),
                                color = Color.LightGray
                            )
                            Text(
                                text = "No",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = nunito,
                                    fontSize = 20.sp,
                                    color = Color.White,
                                    lineHeight = 23.sp,
                                    fontWeight = FontWeight(580),
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier.weight(1f).padding(vertical = 12.dp).clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null
                                ) {
                                    showDeleteDialog = !showDeleteDialog
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

