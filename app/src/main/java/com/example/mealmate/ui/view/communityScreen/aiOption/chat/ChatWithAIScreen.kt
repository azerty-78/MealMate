package com.example.mealmate.ui.view.communityScreen.aiOption.chat

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mealmate.GenerativeViewModelFactory
import com.example.mealmate.R
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatWithAIScreen(
    chatViewModel: ChatViewModel = viewModel(factory = GenerativeViewModelFactory),
    onBackClick: () -> Unit = {}
) {
    val chatUiState by chatViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var showMenu by remember { mutableStateOf(false) }

    // Auto-scroll vers le bas quand un nouveau message arrive
    LaunchedEffect(chatUiState.messages.size) {
        if (chatUiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.chat_with_ai_filled),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Coach AI Nutritionnel",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = if (chatUiState.isTyping) "En train d'écrire..." else "En ligne",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (chatUiState.isTyping)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant//Color.GREEN
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Vider la conversation") },
                                onClick = {
                                    chatViewModel.clearChat()
                                    showMenu = false
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Delete, contentDescription = null)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Régénérer la réponse") },
                                onClick = {
                                    chatViewModel.regenerateResponse()
                                    showMenu = false
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Refresh, contentDescription = null)
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            EnhancedMessageInput(
                onSendMessage = { inputText ->
                    chatViewModel.sendMessage(inputText)
                },
                resetScroll = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (chatUiState.messages.isEmpty()) {
                WelcomeScreen()
            } else {
                ChatList(
                    chatMessages = chatUiState.messages,
                    listState = listState,
                    isTyping = chatUiState.isTyping
                )
            }
        }
    }
}

@Composable
fun WelcomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.chat_with_ai_filled),
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Bienvenue chez votre Coach AI !",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Je suis là pour vous aider avec vos questions sur la nutrition, l'alimentation saine et le bien-être.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Suggestions de démarrage
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(getSuggestions()) { suggestion ->
                SuggestionCard(suggestion = suggestion)
            }
        }
    }
}

@Composable
fun SuggestionCard(suggestion: String) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            text = suggestion,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ChatList(
    chatMessages: List<ChatMessage>,
    listState: LazyListState,
    isTyping: Boolean = false
) {
    LazyColumn(
        reverseLayout = true,
        state = listState,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Indicateur de frappe
        if (isTyping) {
            item {
                TypingIndicator()
            }
        }

        items(chatMessages.reversed()) { message ->
            EnhancedChatBubbleItem(message)
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(3) { index ->
                    val alpha by animateFloatAsState(
                        targetValue = if ((LocalTime.now().nano / 500_000_000 + index) % 3 == 0) 1f else 0.3f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(600),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "typing_animation"
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = alpha)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun EnhancedChatBubbleItem(chatMessage: ChatMessage) {
    val isModelMessage = chatMessage.participant == Participant.MODEL ||
            chatMessage.participant == Participant.ERROR

    val backgroundColor = when (chatMessage.participant) {
        Participant.MODEL -> MaterialTheme.colorScheme.primaryContainer
        Participant.USER -> MaterialTheme.colorScheme.tertiaryContainer
        Participant.ERROR -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = when (chatMessage.participant) {
        Participant.MODEL -> MaterialTheme.colorScheme.onPrimaryContainer
        Participant.USER -> MaterialTheme.colorScheme.onTertiaryContainer
        Participant.ERROR -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val bubbleShape = if (isModelMessage) {
        RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
    } else {
        RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
    }

    val horizontalAlignment = if (isModelMessage) {
        Alignment.Start
    } else {
        Alignment.End
    }

    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (isModelMessage)
                Arrangement.Start else Arrangement.End
        ) {
            if (chatMessage.isPending) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                shape = bubbleShape,
                modifier = Modifier.widthIn(0.dp, LocalConfiguration.current.screenWidthDp.dp * 0.85f)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = chatMessage.text,
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = formatTime(chatMessage.timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor.copy(alpha = 0.7f),
                        modifier = Modifier.align(
                            if (isModelMessage) Alignment.Start else Alignment.End
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun EnhancedMessageInput(
    onSendMessage: (String) -> Unit,
    resetScroll: () -> Unit = {}
) {
    var userMessage by rememberSaveable { mutableStateOf("") }
    val isMessageEmpty = userMessage.isBlank()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Column {
            Divider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                OutlinedTextField(
                    value = userMessage,
                    label = { Text("Tapez votre message...") },
                    onValueChange = { userMessage = it },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 4
                )

                FloatingActionButton(
                    onClick = {
                        if (!isMessageEmpty) {
                            onSendMessage(userMessage.trim())
                            userMessage = ""
                            resetScroll()
                        }
                    },
                    modifier = Modifier.size(48.dp),
                    containerColor = if (isMessageEmpty)
                        MaterialTheme.colorScheme.surfaceVariant
                    else MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Envoyer",
                        tint = if (isMessageEmpty)
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

// Fonctions utilitaires
private fun getSuggestions(): List<String> {
    return listOf(
        "Comment créer un plan alimentaire équilibré ?",
        "Quels sont les aliments riches en protéines ?",
        "Comment gérer les fringales ?",
        "Conseils pour une alimentation végétarienne",
        "Comment calculer mes besoins caloriques ?"
    )
}

private fun formatTime(timestamp: LocalDateTime): String {
    val now = LocalDateTime.now()
    return when {
        timestamp.toLocalDate() == now.toLocalDate() -> {
            "${timestamp.hour.toString().padStart(2, '0')}:${timestamp.minute.toString().padStart(2, '0')}"
        }
        timestamp.toLocalDate() == now.toLocalDate().minusDays(1) -> {
            "Hier ${timestamp.hour.toString().padStart(2, '0')}:${timestamp.minute.toString().padStart(2, '0')}"
        }
        else -> {
            "${timestamp.dayOfMonth}/${timestamp.monthValue} ${timestamp.hour.toString().padStart(2, '0')}:${timestamp.minute.toString().padStart(2, '0')}"
        }
    }
}