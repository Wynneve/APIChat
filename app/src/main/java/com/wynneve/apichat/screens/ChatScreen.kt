package com.wynneve.apichat.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wynneve.apichat.R
import com.wynneve.apichat.composables.CustomTextField
import com.wynneve.apichat.composables.HeaderRow
import com.wynneve.apichat.db.entities.DbMessage
import com.wynneve.apichat.ui.theme.APIChatTheme
import com.wynneve.apichat.ui.theme.colorTimestamp
import com.wynneve.apichat.ui.theme.colorUserMessage
import com.wynneve.apichat.viewmodels.ChatViewModel
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*
import android.util.Base64
import android.util.Base64OutputStream
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.core.content.FileProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.wynneve.apichat.composables.ContentListColumn
import com.wynneve.apichat.ui.theme.colorDelete
import com.wynneve.apichat.ui.theme.colorInactive
import com.wynneve.apichat.ui.theme.colorLogout
import com.wynneve.apichat.ui.theme.colorShadow
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.reflect.*
import kotlin.reflect.full.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class
)
@Composable
fun ChatScreen(chatViewModel: ChatViewModel) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val imeSource = WindowInsets.imeAnimationSource.getBottom(LocalDensity.current)
    val imeTarget = WindowInsets.imeAnimationTarget.getBottom(LocalDensity.current)
    val barOffset = WindowInsets.systemBars.getBottom(LocalDensity.current)
    var scroll by remember { mutableStateOf(0) }
    LaunchedEffect(imeTarget, imeSource) {
        scope.launch {
            if(imeSource != imeTarget) {
                scroll = if(imeTarget > 0) {
                    imeTarget - barOffset
                } else {
                    max(barOffset - imeSource, scrollState.value - scrollState.maxValue)
                }
                Log.d("Scroll", "${scroll}")
            } else {
                scrollState.animateScrollTo(scrollState.value + scroll)
            }
        }
    }

    val messages by chatViewModel.messages.collectAsState()
    var navigationEnabled by remember { mutableStateOf(true) }
    LaunchedEffect(navigationEnabled) {
        delay(1000)
        navigationEnabled = true
    }

    LaunchedEffect(messages.lastOrNull()?.timestamp) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    BackHandler(enabled = chatViewModel.editingMessage != 0) {
        chatViewModel.editingMessage = 0
        chatViewModel.editingImage = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        HeaderRow(
            title = {
                CustomTextField(
                    value = { chatViewModel.title },
                    onValueChange = { newText ->
                        chatViewModel.chat?.title = newText
                        chatViewModel.title = newText
                        chatViewModel.saveChat(chatViewModel.chat!!, {})
                    },
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    textStyle = MaterialTheme.typography.displayLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                )
            },
            navigation = {
                IconButton(
                    modifier = Modifier.size(40.dp),
                    onClick = {
                        navigationEnabled = false
                        chatViewModel.navigateBack()
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        imageVector = Icons.Default.ArrowBack,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "Back",
                    )
                }
            },
            actions = {
                IconButton(
                    modifier = Modifier.size(40.dp),
                    onClick = {
                        chatViewModel.navigateToSettings()
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        imageVector = Icons.Default.Settings,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "Chat settings",
                    )
                }
            }
        )


        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            messages.forEach { message ->
                ChatMessage(chatViewModel, message)
            }

            val infiniteTransition = rememberInfiniteTransition()
            val dots = infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 4f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 2000, easing = LinearEasing)
                )
            )

            if(chatViewModel.assistantIsTyping) {
                Text(
                    text = LocalContext.current.getString(R.string.chat_Typing) + ".".repeat((dots.value).toInt()),
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontStyle = FontStyle.Italic
                    ),
                )
            }

            if(messages.lastOrNull()?.role == "assistant" && !chatViewModel.assistantIsTyping && chatViewModel.editingMessage == 0) Box(
                modifier = Modifier
                    .size(30.dp, 20.dp)
                    .align(Alignment.Start)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(5.dp)
                    ),
            ) {
                IconButton(
                    onClick = {
                        chatViewModel.onContinueClick()
                    },
                ) {
                    Box {
                        Icon(
                            modifier = Modifier
                                .size(15.dp)
                                .offset(x = (-2.5).dp),
                            imageVector = Icons.Default.KeyboardArrowRight,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = "Continue",
                        )
                        Icon(
                            modifier = Modifier
                                .size(15.dp)
                                .offset(x = (2.5).dp),
                            imageVector = Icons.Default.KeyboardArrowRight,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = "Continue",
                        )
                    }
                }
            }
        }

        BottomBar(chatViewModel = chatViewModel)
    }

    SourceSelect(chatViewModel = chatViewModel)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SourceSelect(chatViewModel: ChatViewModel) {
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    val photoUri = remember {
        val photoFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo.jpg")
        FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile)
    }

    val launcherCamera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(photoUri))
            bitmap?.let {
                saveBitmap(it, chatViewModel)
            }
        }
    }
    val launcherGallery = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            saveBitmap(bitmap, chatViewModel)
        }
    }

    val animatedColor by animateColorAsState(
        targetValue = if(chatViewModel.pickingSource) colorShadow else Color.Transparent,
        animationSpec = tween(durationMillis = 250, easing = LinearEasing)
    )

    if(chatViewModel.pickingSource) Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = animatedColor)
            .clickable(
                enabled = true,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                chatViewModel.pickingSource = false
            },
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(fraction = (2f / 3f))
                    .clickable(enabled = false) {}
            ) {
                ContentListColumn {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 5.dp),
                        text = LocalContext.current.getString(R.string.chat_SelectSource)
                    )

                    Button(
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        ),
                        onClick = {
                            launcherGallery.launch("image/*")
                            chatViewModel.pickingSource = false
                        }
                    ) {
                        Text(
                            text = LocalContext.current.getString(R.string.chat_Gallery),
                            style = MaterialTheme.typography.displayMedium
                        )
                    }

                    Button(
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        ),
                        onClick = {
                            if (cameraPermissionState.status.isGranted) {
                                launcherCamera.launch(photoUri)
                                chatViewModel.pickingSource = false
                            } else {
                                cameraPermissionState.launchPermissionRequest()
                            }
                        }
                    ) {
                        Text(
                            text = LocalContext.current.getString(R.string.chat_Camera),
                            style = MaterialTheme.typography.displayMedium
                        )
                    }
                }
            }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BottomBar(chatViewModel: ChatViewModel) {
    val isEditing by rememberUpdatedState(chatViewModel.editingMessage != 0)

    Row(
        modifier = Modifier
            .heightIn(min = 60.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        IconButton(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.Top),
            onClick = {
                if(isEditing) {
                    if(chatViewModel.editingImage != null) {
                        chatViewModel.editingImage = null
                    } else {
                        chatViewModel.pickingSource = true
                    }
                } else {
                    if(chatViewModel.currentImage != null) {
                        chatViewModel.currentImage = null
                    } else {
                        chatViewModel.pickingSource = true
                    }
                }
            }
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector =
                    if((if(isEditing) chatViewModel.editingImage else chatViewModel.currentImage) != null) Icons.Default.Clear
                    else Icons.Default.Add,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = "Attachment",
            )
        }

        CustomTextField(
            boxModifier = Modifier
                .weight(1f)
                .heightIn(min = 40.dp),
            shape = RoundedCornerShape(5.dp),
            fieldModifier = Modifier.fillMaxWidth(),
            value = if(isEditing) ({
                 chatViewModel.editingContent
            }) else ({
                chatViewModel.currentMessage
            }),
            onValueChange = { newText ->
                if(isEditing) chatViewModel.editingContent = newText
                else chatViewModel.currentMessage = newText
            },
            placeholder = LocalContext.current.getString(R.string.chat_Message),
            enabled = true,
            singleLine = false,
            horizontalPadding = 10.dp,
            verticalPadding = 5.dp,
            minLines = 1,
            maxLines = 5,
        )

        val isEmpty by rememberUpdatedState(
            if(isEditing) chatViewModel.editingContent.isBlank()
            else chatViewModel.currentMessage.isBlank()
        )

        IconButton(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.Top),
            onClick = {
                if(isEditing) chatViewModel.onSaveClick()
                else chatViewModel.onSendClick()
            },
            enabled = !isEmpty && (!chatViewModel.assistantIsTyping || chatViewModel.editingMessage != 0)
        ) {
            Icon(
                modifier = Modifier.size(25.dp),
                imageVector = if(isEditing) Icons.Default.Check else Icons.Default.Send,
                tint = if(isEmpty || (chatViewModel.assistantIsTyping && chatViewModel.editingMessage == 0)) colorInactive else MaterialTheme.colorScheme.onSurface,
                contentDescription = "Send",
            )
        }
    }
}

@Composable
fun ChatMessage(chatViewModel: ChatViewModel, message: DbMessage) {
    val time = Date(message.timestamp)
    val dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())
    val formattedTime = dateFormat.format(time)
    val surfaceColor = if (message.role == "assistant") {
        MaterialTheme.colorScheme.surface
    } else {
        colorUserMessage
    }
    val hasImage = message.image != null
    val image = if(hasImage) Base64.decode(message.image, Base64.DEFAULT) else null
    val bitmap = if(hasImage) BitmapFactory.decodeByteArray(image, 0, image!!.size) else null

    ChatMessageBase(
        chatViewModel,
        id = message.id,
        content = message.content,
        formattedTime = formattedTime,
        surfaceColor = surfaceColor,
        right = message.role == "user",
        hasImage = hasImage,
        image = message.image,
        bitmap = bitmap
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatMessageBase(chatViewModel: ChatViewModel, id: Int, content: String, formattedTime: String, surfaceColor: Color, right: Boolean, hasImage: Boolean, image: String?, bitmap: Bitmap?) {
    val isEditing = chatViewModel.editingMessage == id

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {

                    },
                    onLongClick = {
                        if (!isEditing && !chatViewModel.assistantIsTyping) {
                            chatViewModel.editingMessage = id
                            chatViewModel.editingContent = content
                            chatViewModel.editingImage = image
                        }
                    }
                ),
            horizontalArrangement = if (right) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .wrapContentWidth(align = if (right) Alignment.End else Alignment.Start)
                    .background(
                        surfaceColor, shape = RoundedCornerShape(
                            10.dp, 10.dp,
                            if (right) 0.dp else 10.dp,
                            if (right) 10.dp else 0.dp
                        )
                    )
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                MarkdownText(
                    markdown = content,
                    isTextSelectable = true,
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                )

                if (hasImage) {
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "Attached image.",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp)
                    )
                }

                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = formattedTime,
                    color = colorTimestamp,
                    style = MaterialTheme.typography.displaySmall,
                )
            }
        }

        if (isEditing) {
            DeleteButton(
                chatViewModel = chatViewModel,
                modifier = Modifier
                    .align(if (right) Alignment.CenterStart else Alignment.CenterEnd)
            )
        }
    }
}

@Composable
fun DeleteButton(chatViewModel: ChatViewModel, modifier: Modifier = Modifier) {
    IconButton(
        modifier = modifier
            .size(25.dp),
        onClick = { chatViewModel.onDeleteClick() }
    ) {
        Icon(
            modifier = Modifier
                .size(25.dp),
            tint = colorDelete,
            imageVector = Icons.Default.Delete,
            contentDescription = "",
        )
    }
}

fun saveBitmap(bitmap: Bitmap, chatViewModel: ChatViewModel) {
    val scalingFactor = min(1f, sqrt((1024f * 1024f) / (bitmap.width * bitmap.height)))
    val newWidth = (bitmap.width * scalingFactor).toInt()
    val newHeight = (bitmap.height * scalingFactor).toInt()
    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    val outputStream = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val base64String = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)

    if(chatViewModel.editingMessage != 0) chatViewModel.editingImage = base64String
    else chatViewModel.currentImage = base64String
}

@Composable
@Preview
fun ChatScreenPreview() {
    val chatViewModel = viewModel { ChatViewModel(0, {}, {}, {}, { _, _ -> }) }
    chatViewModel.currentMessage = "Hello again?"
    chatViewModel.assistantIsTyping = true

    APIChatTheme {
        Surface {
            ChatScreen(chatViewModel)
        }
    }
}