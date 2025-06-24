package com.example.mealmate.ui.view.communityScreen.aiOption.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.asTextOrNull
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



class ChatViewModel(
    private val generativeModel: GenerativeModel
) : ViewModel(){

    private val chat = generativeModel.startChat(
        history = listOf(
            content(role = "user") {
                text("Bonjour, je cherche des conseils pour une alimentation saine")
            },
            content(role = "model") {
                text("Bonjour ! Je suis votre coach AI nutritionnel. Je serai ravi de vous aider avec vos questions sur l'alimentation et la santé. Pouvez-vous me parler de vos objectifs nutritionnels ?")
            }
        )
    )

    private val _uiState: MutableStateFlow<ChatUiState> =
        MutableStateFlow(ChatUiState(chat.history.map { content ->
            ChatMessage(
                text = content.parts.first().asTextOrNull() ?: "",
                participant = if (content.role == "user") Participant.USER else Participant.MODEL,
                isPending = false
            )
        }))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun sendMessage(userMessage: String) {
        // Ajouter le message de l'utilisateur
        _uiState.value.addMessage(
            ChatMessage(
                text = userMessage,
                participant = Participant.USER,
                isPending = false
            )
        )

        // Afficher l'indicateur de frappe
        _uiState.value.setTypingStatus(true)

        viewModelScope.launch {
            try {
                // Simuler un délai de frappe
                delay(1000)

                val response = chat.sendMessage(userMessage)
                _uiState.value.setTypingStatus(false)

                response.text?.let { modelResponse ->
                    _uiState.value.addMessage(
                        ChatMessage(
                            text = modelResponse,
                            participant = Participant.MODEL,
                            isPending = false
                        )
                    )
                }
            } catch (e: Exception) {
                _uiState.value.setTypingStatus(false)
                _uiState.value.addMessage(
                    ChatMessage(
                        text = "Désolé, je rencontre des difficultés techniques. Veuillez réessayer.",
                        participant = Participant.ERROR
                    )
                )
            }
        }
    }

    fun clearChat() {
        _uiState.value.clearMessages()
    }

    fun regenerateResponse() {
        // Implémenter la régénération de la dernière réponse
        val messages = _uiState.value.messages
        if (messages.isNotEmpty() && messages.last().participant == Participant.MODEL) {
            val lastUserMessage = messages.dropLast(1).lastOrNull { it.participant == Participant.USER }
            lastUserMessage?.let {
                // Supprimer la dernière réponse du modèle
                _uiState.value._messages.removeAt(_uiState.value._messages.size - 1)
                // Renvoyer le message
                sendMessage(it.text)
            }
        }
    }
}
