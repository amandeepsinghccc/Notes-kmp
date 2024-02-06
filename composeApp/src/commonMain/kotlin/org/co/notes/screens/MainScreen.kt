package org.co.notes.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.co.notes.MainViewModel
import org.co.notes.font
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.NoteColorCyan
import theme.NoteColorGreen
import theme.NoteColorPink
import theme.NoteColorPurple
import theme.NoteColorSkin
import theme.NoteColorYellow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun MainScreen(mainViewModel: MainViewModel) {
    val nunito =
        FontFamily(font("Nunito", "nunito_regular.ttf", FontWeight.Normal, FontStyle.Normal))
    var currentScreen by remember {
        mutableStateOf(CurrentScreen.MAIN_SCREEN)
    }
    var colorList by remember {
        mutableStateOf(
            listOf<Color>(
                NoteColorCyan, NoteColorGreen, NoteColorPink,
                NoteColorYellow, NoteColorSkin, NoteColorPurple
            )
        )
    }
    var state = rememberTopAppBarState()
    val noteList by mainViewModel.notesList.collectAsState()
    LaunchedEffect(noteList) {
        print("*******" + noteList)
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    if (currentScreen == CurrentScreen.MAIN_SCREEN) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.padding(end = 5.dp, bottom = 5.dp).size(60.dp)
                        .bounceClick(),
                    containerColor = Color(0xFFFDB600),
                    content = {
                        Icon(
                            painter = painterResource("add.xml"),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    shape = CircleShape,
                    onClick = {
                        currentScreen = CurrentScreen.ADD_NOTE
                    })
            },
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    modifier = Modifier.padding(top = 20.dp),
                    title = {
                        Text(
                            "Notes",
                            fontFamily = nunito,
                            color = Color.White,
                            fontSize = 35.sp,
                            fontWeight = FontWeight.SemiBold
                        )
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
                            WrappedIcon(Icons.Default.Search) {}
                            WrappedIcon(Icons.Outlined.Info) {}
                        }
                    }
                )
            },
            containerColor = Color(0xFF252525)
        ) {
            LazyColumn(modifier = Modifier.padding(it)) {
                items(noteList.notesList) {
                    Note(it.title, nunito, colorList.random())
                }
            }
        }
    } else {
        EditScreen(mainViewModel) {
            currentScreen = CurrentScreen.MAIN_SCREEN
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EditScreen(viewModel: MainViewModel, onBackClick: () -> Unit) {
    val nunito =
        FontFamily(font("Nunito", "nunito_regular.ttf", FontWeight.Normal, FontStyle.Normal))
    var title by remember {
        mutableStateOf("")
    }
    var body by remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboard = LocalSoftwareKeyboardController.current

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, snackbar = { Snackbar(snackbarData = it, containerColor = Color.White, contentColor = Color.Black) })
        },
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(top = 20.dp),
                title = {
                    WrappedIcon(Icons.Default.ArrowBack) {
                        onBackClick()
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
                        WrappedIcon("visibility.xml") {}
                        WrappedIcon(Icons.Default.Done) {
                            keyboard?.hide()
                            if (title.isNotEmpty()) {
                                viewModel.createNote(title, body)
                                onBackClick()
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
                value = title,
                onValueChange = { inputText ->
                    if (inputText.length < 70) {
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
                    focusedIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = nunito,
                    fontSize = 30.sp,
                    color = Color.White,
                    lineHeight = 35.sp,
                    fontWeight = FontWeight(550)
                )
            )

            TextField(
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
                    focusedIndicatorColor = Color.Transparent
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
    }
}

enum class ButtonState { Pressed, Idle }

fun Modifier.bounceClick() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(
        if (buttonState == ButtonState.Pressed) 0.90f else 1f,
        label = ""
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { }
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}

enum class CurrentScreen {
    MAIN_SCREEN,
    ADD_NOTE,
    VIEW_NOTE
}

@Composable
fun WrappedIcon(icon: ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier.bounceClick().background(
            color = Color.White.copy(alpha = .1f),
            shape = AbsoluteRoundedCornerShape(10.dp)
        )
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.padding(10.dp).size(22.dp)
                .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                    onClick()
                }
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun WrappedIcon(resource: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.bounceClick().background(
            color = Color.White.copy(alpha = .1f),
            shape = AbsoluteRoundedCornerShape(10.dp)
        )
    ) {
        Icon(
            painter = painterResource(resource),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.padding(10.dp).size(22.dp)
                .clickable(indication = null, interactionSource = MutableInteractionSource()) {
                    onClick()
                }
        )
    }
}

@Composable
fun Note(header: String, nunito: FontFamily, color: Color) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 8.dp)
            .background(color = color, shape = AbsoluteRoundedCornerShape(20f))
    ) {
        Text(
            text = header,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 25.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = nunito,
                fontSize = 25.sp,
                color = Color.Black,
                fontWeight = FontWeight(550),
                lineHeight = 28.sp
            )
        )
    }
}