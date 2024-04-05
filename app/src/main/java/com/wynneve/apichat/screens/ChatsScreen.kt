package com.wynneve.apichat.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wynneve.apichat.composables.ContentListColumn
import com.wynneve.apichat.composables.HeaderRow
import com.wynneve.apichat.db.entities.DbChat
import com.wynneve.apichat.ui.theme.APIChatTheme
import com.wynneve.apichat.ui.theme.colorDelete

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen() {
    val isInEditMode = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        HeaderRow(
            title = "Chats",
            actions = {
                if (!isInEditMode.value) {
                    IconButton(
                        modifier = Modifier
                            .size(40.dp),
                        onClick = {}
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
                        onClick = { isInEditMode.value = !isInEditMode.value }
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
                } else null
            }
        )

        val chats = Array<DbChat>(20) {
                index -> DbChat(index, 0, "chat${index}", 0L)
        }

        if(chats.isEmpty()) return

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 10.dp)
        ) {
            ContentListColumn {
                for(chat in chats) {
                    ChatEntry(chat, {}, isInEditMode.value)
                }
            }
        }
    }
}

@Composable
fun ChatEntry(chat: DbChat, onClick: () -> Unit, isInEditMode: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(5.dp)
            )
            .height(40.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(start = 10.dp),
            style = MaterialTheme.typography.displayMedium,
            text = chat.title
        )

        IconButton(
            modifier = Modifier
                .padding(end = 5.dp)
                .size(25.dp),
            onClick = {}
        ) {
            if(isInEditMode) {
                Icon(
                    modifier = Modifier
                        .size(25.dp),
                    tint = colorDelete,
                    imageVector = Icons.Default.Delete,
                    contentDescription = "",
                )
            } else {
                Icon(
                    modifier = Modifier
                        .size(25.dp),
                    tint = MaterialTheme.colorScheme.onBackground,
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
        ChatsScreen()
    }
}