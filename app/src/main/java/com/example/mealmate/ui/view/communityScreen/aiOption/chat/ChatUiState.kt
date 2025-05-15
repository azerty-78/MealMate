package com.example.mealmate.ui.view.communityScreen.aiOption.chat

import androidx.compose.runtime.toMutableStateList

class ChatUiState(
    messages: List<ChatMessage> = emptyList()
) {
    private val _messages: MutableList<ChatMessage> = messages.toMutableStateList()
    val messages: List<ChatMessage> = _messages

    fun addMessage(msg: ChatMessage) {
        _messages.add(msg)
    }

    //@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun replaceLastPendingMessage() {
        val lastMessage = _messages.lastOrNull()
        lastMessage?.let {
            val newMessage = lastMessage.apply { isPending = false }
            _messages.removeAt(_messages.size - 1)
            _messages.add(newMessage)
        }
    }
}

