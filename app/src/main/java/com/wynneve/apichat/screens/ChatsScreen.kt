package com.wynneve.apichat.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.wynneve.apichat.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wynneve.apichat.composables.ContentListColumn
import com.wynneve.apichat.composables.HeaderRow
import com.wynneve.apichat.composables.LoadingPopup
import com.wynneve.apichat.composables.NamedTextField
import com.wynneve.apichat.db.entities.DbChat
import com.wynneve.apichat.ui.theme.APIChatTheme
import com.wynneve.apichat.ui.theme.colorDelete
import com.wynneve.apichat.ui.theme.colorShadow
import com.wynneve.apichat.viewmodels.ChatsViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatsScreen(chatsViewModel: ChatsViewModel) {
    val chats by chatsViewModel.chats.collectAsState(initial = emptyList())

    BackHandler(enabled = chatsViewModel.isEditing) {
        chatsViewModel.isEditing = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        val newChat = LocalContext.current.getString(R.string.chats_NewChat)
        HeaderRow(
            title = LocalContext.current.getString(R.string.chats_Title),
            actions = {
                if (!chatsViewModel.isEditing) {
                    IconButton(
                        modifier = Modifier
                            .size(40.dp),
                        onClick = { chatsViewModel.createChat(newChat) }
                    ) {
                        Icon(
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp),
                            tint = MaterialTheme.colorScheme.onSurface,
                            imageVector = Icons.Default.Add,
                            contentDescription = "",
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    IconButton(
                        modifier = Modifier
                            .size(40.dp),
                        onClick = {
                            chatsViewModel.navigateToSettings()
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .width(25.dp)
                                .height(25.dp),
                            tint = MaterialTheme.colorScheme.onSurface,
                            imageVector = Icons.Default.Settings,
                            contentDescription = "",
                        )
                    }
                } else {
                    IconButton(
                        modifier = Modifier
                            .size(40.dp),
                        onClick = { chatsViewModel.isEditing = false }
                    ) {
                        Icon(
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp),
                            tint = MaterialTheme.colorScheme.onSurface,
                            imageVector = Icons.Default.Check,
                            contentDescription = "",
                        )
                    }
                }
            }
        )

        if(chats.isEmpty()) return

        ContentListColumn {
            for(chat in chats) {
                ChatEntry(
                    chat = chat,
                    chatsViewModel = chatsViewModel
                )
            }
        }
    }

    LoadingPopup(!chatsViewModel.synced)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatEntry(
    chat: DbChat,
    chatsViewModel: ChatsViewModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(5.dp)
            )
            .height(40.dp)
            .combinedClickable(
                onClick = {
                    if (!chatsViewModel.isEditing) {
                        chatsViewModel.navigateToChat(chat.id) {}
                    }
                },
                onLongClick = {
                    chatsViewModel.isEditing = true
                }
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(start = 10.dp),
            style = MaterialTheme.typography.displayMedium,
            text = chat.title
        )

        val alpha by animateFloatAsState(
            targetValue = if(chatsViewModel.isEditing) 1f else 0f,
            animationSpec = tween(durationMillis = 500)
        )

        Crossfade(targetState = chatsViewModel.isEditing) {
            if (it) IconButton(
                modifier = Modifier
                    .padding(end = 5.dp)
                    .size(25.dp),
                onClick = { chatsViewModel.deleteChat(chat.id) }
            ) {
                Icon(
                    modifier = Modifier
                        .size(25.dp),
                    tint = colorDelete.copy(alpha = alpha),
                    imageVector = Icons.Default.Delete,
                    contentDescription = "",
                )
            } else Box(
                modifier = Modifier
                    .padding(end = 5.dp)
                    .size(25.dp),
            ) {
                Icon(
                    modifier = Modifier
                        .size(25.dp),
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 1f - alpha),
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "",
                )
            }
        }
    }
}

@Preview
@Composable
fun ChatsScreenPreview() {
    APIChatTheme {
        val viewModel = ChatsViewModel(
            profileId = 0,
            navigateToChat = { _, _ -> },
            navigateToSettings = {}
        )

        ChatsScreen(viewModel)
    }
}