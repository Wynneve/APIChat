package com.example.apichat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apichat.ui.theme.APIChatTheme
import com.example.apichat.ui.theme.colorPlaceholder
import com.example.apichat.ui.theme.colorTimestamp
import com.example.apichat.ui.theme.colorUserMessage
import dev.jeziellago.compose.markdowntext.MarkdownText
import java.text.DateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(chat: Chat, controller: ChatController, settings: SettingsViewModel) {
    controller.scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HeaderRow {
            IconButton(
                modifier = Modifier
                    .height(40.dp),
                onClick=controller::onSettingsClick
            ) {
                Icon(
                    modifier = Modifier,
                    imageVector = Icons.Default.Settings,
                    contentDescription = LocalContext.current.getString(R.string.chat_Message),
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
                .verticalScroll(controller.scrollState!!),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            for(message in chat.messages) {
                ChatMessage(message)
            }
            if(chat.assistantIsTyping.value) {
                Text(
                    text = "${settings.get(Setting.botName)} ${LocalContext.current.getString(R.string.chat_IsTyping)}"
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CustomizableTextField(
                boxModifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp),
                backgroundColor = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(5.dp),
                textModifier = Modifier.fillMaxWidth(),
                value = { chat.currentMessage.value },
                onValueChange = controller::onMessageType,
                textStyle = MaterialTheme.typography.displayMedium,
                textColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.onBackground,
                placeholder = LocalContext.current.getString(R.string.chat_Message),
                enabled = true,
                singleLine = false,
                horizontalPadding = 5.dp,
                verticalPadding = 5.dp,
                placeholderColor = colorPlaceholder,
                minLines = 1,
                maxLines = 5,
            )

            Box(
                modifier=Modifier
                    .align(Alignment.Top)
            ) {
                IconButton(
                    modifier = Modifier
                        .height(40.dp),
                    onClick=controller::onSendClick
                ) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.Center),
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

    if(message.role == Role.Assistant) {
        ChatMessageBase(content = message.content, formattedTime = formattedTime, surfaceColor = MaterialTheme.colorScheme.surface, right = false)
    }
    else if(message.role == Role.User) {
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
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val settings = viewModel { SettingsViewModel(context, scope, {}) }

    val chat = Chat()
    val controller = ChatController(chat, settings, LocalContext.current, rememberCoroutineScope(), {})

    chat.messages.add(Message(Role.User, Date(2024, 1, 1), "Hello! Could you please write a program in Python for me?"))
    chat.messages.add(Message(Role.Assistant, Date(2024, 1, 1), "Yea, for example, this Bubble Sort will fit:\n```python\ndef bubble(arr):\n```"))
    chat.currentMessage.value = "Hello again?"
    chat.assistantIsTyping.value = true
    controller.scrollState = rememberScrollState()

    APIChatTheme() {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color=MaterialTheme.colorScheme.background
        ) {
            ChatScreen(chat = chat, controller = controller, settings = settings)
        }
    }
}