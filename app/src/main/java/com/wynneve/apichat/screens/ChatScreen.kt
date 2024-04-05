package com.example.apichat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults.ContentPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wynneve.apichat.composables.CustomTextField
import com.wynneve.apichat.composables.HeaderRow
import com.wynneve.apichat.db.entities.DbMessage
import com.wynneve.apichat.roleAssistant
import com.wynneve.apichat.roleUser
import com.wynneve.apichat.ui.theme.APIChatTheme
import com.wynneve.apichat.ui.theme.colorPlaceholder
import com.wynneve.apichat.ui.theme.colorTimestamp
import com.wynneve.apichat.ui.theme.colorUserMessage
import dev.jeziellago.compose.markdowntext.MarkdownText
import java.text.DateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random
import kotlin.random.Random.Default.nextLong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        // Top bar
        HeaderRow(
            title = "Chat",
            navigation = {
                IconButton(
                    modifier = Modifier
                        .size(40.dp),
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier
                            .size(25.dp),
                        imageVector = Icons.Default.ArrowBack,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "Back",
                    )
                }
            },
            actions = {
                IconButton(
                    modifier = Modifier
                        .size(40.dp),
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier
                            .size(25.dp),
                        imageVector = Icons.Default.Settings,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "Chat settings",
                    )
                }
            }
        )

        // Messages column
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(all = 10.dp)
                .verticalScroll(
                    state = rememberScrollState()
                ),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            val messages = Array<DbMessage>(10) {
                DbMessage(it, 0, if(Random.nextInt() % 2 == 0) "assistant" else "user", Random.nextLong(), "Very long message to surely overfit the boundaries ${it}", null)
            }
            val isTyping = false

            for (message in messages) {
                ChatMessage(message)
            }
        }

        // Bottom bar
        Row(
            modifier = Modifier
                .heightIn(min = 60.dp)
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconButton(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .align(Alignment.Top),
                onClick = { },
            ) {
                Icon(
                    modifier = Modifier
                        .size(30.dp),
                    imageVector = Icons.Default.Add,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Send",
                )
            }

            CustomTextField(
                boxModifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp),
                shape = RoundedCornerShape(5.dp),
                fieldModifier = Modifier
                    .fillMaxWidth(),
                value = { "abc" },
                onValueChange = { },
                placeholder = LocalContext.current.getString(R.string.chat_Message),
                enabled = true,
                singleLine = false,
                horizontalPadding = 10.dp,
                verticalPadding = 5.dp,
                minLines = 1,
                maxLines = 5,
            )

            IconButton(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Top),
                onClick = { },
            ) {
                Icon(
                    modifier = Modifier
                        .size(25.dp),
                    imageVector = Icons.Default.Send,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "Send",
                )
            }
        }
    }
}

@Composable
fun ChatMessage(message: DbMessage) {
    val time = Date(message.timestamp)
    val dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())
    val formattedTime = dateFormat.format(time)

    if(message.role == roleAssistant) {
        ChatMessageBase(content = message.content, formattedTime = formattedTime, surfaceColor = MaterialTheme.colorScheme.surface, right = false)
    }
    else if(message.role == roleUser) {
        ChatMessageBase(content = message.content, formattedTime = formattedTime, surfaceColor = colorUserMessage, right = true)
    }
}

@Composable
fun ChatMessageBase(content: String, formattedTime: String, surfaceColor: Color, right: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if(right) Box(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier
                .weight(3f, fill = false)
                .background(
                    surfaceColor, shape = RoundedCornerShape(
                        10.dp, 10.dp,
                        if (right) 0.dp else 10.dp,
                        if (right) 10.dp else 0.dp
                    )
                )
                .padding(horizontal = 10.dp, vertical = 5.dp),
        ) {
            MarkdownText(
                markdown = content,
                isTextSelectable = true,
                style = MaterialTheme.typography.displayMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
            )

            Text(
                modifier = Modifier
                    .align(Alignment.End),
                text = formattedTime,
                color = colorTimestamp,
                style = MaterialTheme.typography.displaySmall,
            )
        }
        if(!right) Box(modifier = Modifier.weight(1f))
    }
}

@Composable
@Preview
fun ChatScreenPreview() {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    //val settings = viewModel { SettingsViewModel(context, scope, {}) }

    //val chat = Chat()
    //val controller = ChatController(chat, settings, LocalContext.current, rememberCoroutineScope(), {})

//    chat.messages.add(Message(roleUser, Date(2024, 1, 1).time, "Hello! Could you please write a program in Python for me?"))
//    chat.messages.add(Message(roleAssistant, Date(2024, 1, 1).time, "Yea, for example, this Bubble Sort will fit:\n```python\ndef bubble(arr):\n```"))
//    chat.currentMessage.value = "Hello again?"
//    chat.assistantIsTyping.value = true
//    controller.scrollState = rememberScrollState()

    APIChatTheme {
        Surface {
            ChatScreen()
        }
    }
}