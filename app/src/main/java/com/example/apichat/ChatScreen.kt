package com.example.apichat

import android.os.*
import android.view.*
import androidx.activity.*
import androidx.activity.compose.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.example.apichat.ui.theme.APIChatTheme
import com.example.apichat.ui.theme.colorPlaceholder
import com.example.apichat.ui.theme.colorTimestamp
import com.example.apichat.ui.theme.colorUserMessage
import dev.jeziellago.compose.markdowntext.MarkdownText
import java.text.DateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chat: Chat) {
    chat.scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface, shape = RectangleShape)
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            IconButton(
                modifier = Modifier
                    .height(40.dp),
                onClick=chat::onSettingsClick
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center),
                    imageVector = Icons.Default.Settings,
                    contentDescription = LocalContext.current.getString(R.string.chat_Message),
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
                .verticalScroll(chat.scrollState!!),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            for(message in chat.messages) {
                ChatMessage(message)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(5.dp),
                    )
            ) {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterStart),
                    value = chat.currentMessage.value,
                    onValueChange = chat::onMessageType,
                    textStyle = MaterialTheme.typography.displayMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                    decorationBox = { innerTextField ->
                        TextFieldDefaults.DecorationBox(
                            value = chat.currentMessage.value,
                            innerTextField = innerTextField,
                            enabled = true,
                            singleLine = false,
                            visualTransformation = VisualTransformation.None,
                            interactionSource = MutableInteractionSource(),
                            placeholder = {
                                if (chat.currentMessage.value.isEmpty()) {
                                    Text(text = LocalContext.current.getString(R.string.chat_Message))
                                }
                            },
                            contentPadding = PaddingValues(horizontal = 5.dp, vertical = 5.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedPlaceholderColor = colorPlaceholder,
                                unfocusedPlaceholderColor = colorPlaceholder,
                            ),
                        )
                    },
                    minLines = 1,
                    maxLines = 5,
                )
            }

            Box(
                modifier=Modifier
                    .align(Alignment.Top)
            ) {
                IconButton(
                    modifier = Modifier
                        .height(40.dp),
                    onClick=chat::onSendClick
                ) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterEnd),
                        imageVector = Icons.Default.Send,
                        contentDescription = LocalContext.current.getString(R.string.chat_Message),
                    )
                }
            }
        }
    }
}

@Composable
fun ChatMessage(message: Message) {
    val time = message.date
    val dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())
    val formattedTime = dateFormat.format(time)

    if(message.author == "bot") {
        ChatMessageBase(content = message.content, formattedTime = formattedTime, surfaceColor = MaterialTheme.colorScheme.surface, right = false)
    }
    else if(message.author == "user") {
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
                .background(surfaceColor, shape = RoundedCornerShape(5.dp))
                .padding(5.dp),
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
    val settings = Settings()
    val settingsController = SettingsController(settings, LocalContext.current, rememberCoroutineScope(), {})
    settingsController.loadSettings()

    val chat = Chat(settings, LocalContext.current, rememberCoroutineScope(), {})

    chat.messages.add(Message("user", Date(2024, 1, 1), "Hello! Could you please write a program in Python for me?"))
    chat.messages.add(Message("bot", Date(2024, 1, 1), "Yea, for example, this Bubble Sort will fit:\n```python\ndef bubble(arr):\n```"))
    chat.currentMessage.value = "Hello again?"
    chat.scrollState = rememberScrollState()

    APIChatTheme() {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color=MaterialTheme.colorScheme.background
        ) {
            ChatScreen(chat = chat)
        }
    }
}