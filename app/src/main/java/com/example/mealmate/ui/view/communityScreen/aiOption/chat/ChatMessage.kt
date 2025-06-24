package com.example.mealmate.ui.view.communityScreen.aiOption.chat

import java.time.LocalDateTime
import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    var text: String = "",
    var isPending: Boolean = false,
    val participant: Participant = Participant.USER,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val isTyping: Boolean = false,
    val messageType: MessageType = MessageType.TEXT,
    val attachmentUrl: String? = null
)

enum class Participant {
    USER, MODEL, ERROR, DOCTOR, COMMUNITY_MEMBER
}

enum class MessageType {
    TEXT, IMAGE, FILE, VOICE_NOTE
}

enum class ConnectionStatus {
    CONNECTED, CONNECTING, DISCONNECTED, ERROR
}