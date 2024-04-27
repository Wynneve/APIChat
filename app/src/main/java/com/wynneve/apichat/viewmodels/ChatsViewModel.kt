package com.wynneve.apichat.viewmodels;

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import com.wynneve.apichat.db.controllers.ChatController
import com.wynneve.apichat.db.controllers.ProfileController
import com.wynneve.apichat.db.entities.DbChat
import com.wynneve.apichat.db.entities.DbProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date

class ChatsViewModel(
    val profileId: Int,
    val navigateToChat: (id: Int, callback: () -> Unit) -> Unit
): ViewModel() {
    private val _chats = MutableStateFlow<List<DbChat>>(emptyList())
    val chats: StateFlow<List<DbChat>> = _chats // ???

    var synced by mutableStateOf(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            ChatController.getChatsByProfile(profileId).collect { chats ->
                viewModelScope.launch(Dispatchers.Main) { synced = false }
                async { _chats.emit(chats) }.await()
                viewModelScope.launch(Dispatchers.Main) { synced = true }
            }
        }
    }

    fun createChat(title: String) {
        synced = false

        viewModelScope.launch(Dispatchers.IO){
            val chat = DbChat(profileId = profileId, title = title, lastAccess = Date().time)
            val success = async { ChatController.createChat(chat) }.await()
            if(!success) throw Error("An error has occurred while creating a profile.")

            viewModelScope.launch(Dispatchers.Main) {
                synced = true
            }
        }
    }

    fun removeChat(id: Int, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            ChatController.deleteChatById(id)

            //chats.removeIf { it.id == id }

            callback()
        }
    }
}