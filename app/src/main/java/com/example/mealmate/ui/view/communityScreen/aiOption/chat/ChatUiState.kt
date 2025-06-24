package com.example.mealmate.ui.view.communityScreen.aiOption.chat

import androidx.compose.runtime.toMutableStateList

class ChatUiState(
    messages: List<ChatMessage> = emptyList()
) {
    internal val _messages: MutableList<ChatMessage> = messages.toMutableStateList()
    val messages: List<ChatMessage> = _messages

    private var _isTyping: Boolean = false
    val isTyping: Boolean = _isTyping

    private var _connectionStatus: ConnectionStatus = ConnectionStatus.CONNECTED
    val connectionStatus: ConnectionStatus = _connectionStatus

    fun addMessage(msg: ChatMessage) {
        _messages.add(msg)
    }

    fun replaceLastPendingMessage() {
        val lastMessage = _messages.lastOrNull()
        lastMessage?.let {
            val newMessage = lastMessage.copy(isPending = false)
            _messages.removeAt(_messages.size - 1)
            _messages.add(newMessage)
        }
    }

    fun setTypingStatus(isTyping: Boolean) {
        _isTyping = isTyping
    }

    fun setConnectionStatus(status: ConnectionStatus) {
        _connectionStatus = status
    }

    fun clearMessages() {
        _messages.clear()
    }
}


