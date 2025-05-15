package com.example.mealmate.ui.view.communityScreen.aiOption.chat

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    var text: String = "",
    var isPending: Boolean = false,
    val participant: Participant = Participant.USER
)

enum class Participant{
    USER, MODEL, ERROR
}
