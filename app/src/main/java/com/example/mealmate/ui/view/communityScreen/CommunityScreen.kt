package com.example.mealmate.ui.view.communityScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mealmate.R
import com.example.mealmate.data.source.model.User
import com.example.mealmate.presentation.navgraph.Routes
import com.example.mealmate.ui.view.components.myBottomAppBar.MyBottomAppBar
import com.example.mealmate.ui.view.components.myBottomAppBar.MyNavigationOnRail
import com.example.mealmate.ui.view.components.myTopAppBar.MyTopBar

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CommunityScreen(
    navController: NavController,
    currentUser: User? = null
) {
    val user = currentUser ?: return

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        if (maxWidth > 600.dp) {
            Row(modifier = Modifier.fillMaxSize()) {
                MyNavigationOnRail(navController = navController)

                // Contenu principal pour les grands écrans
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    val data = getChatOptions()
                    // En-tête
                    CommunityHeader(user = user)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Options de chat en grille pour les grands écrans
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(data) { option ->
                            ChatOptionCard(
                                chatOption = option,
                                onClick = {
                                    handleChatOptionClick(navController, option.type)
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        } else {
            Scaffold(
                topBar = {
                    MyTopBar(
                        user = user,
                        modifier = Modifier,
                        onNotificationClick = {
                            navController.navigate("notifications")
                        },
                        onSearchClick = {
                            navController.navigate("search")
                        }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(Routes.Screen.ChatWithAIScreen.route) {
                                popUpTo(Routes.Screen.CommunityScreen.route) {
                                    saveState = false
                                }
                                launchSingleTop = true
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.chat_with_ai_filled),
                            contentDescription = "Chat avec l'IA",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                bottomBar = {
                    MyBottomAppBar(navController = navController)
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        CommunityHeader(user = user)
                    }

                    item {
                        QuickActionsSection(navController = navController)
                    }

                    item {
                        ChatOptionsSection(navController = navController)
                    }

                    item {
                        RecentConversationsSection(navController = navController)
                    }

                    item {
                        CommunityStatsSection()
                    }
                }
            }
        }
    }
}

@Composable
fun CommunityHeader(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenue dans votre Communauté",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Connectez-vous avec votre médecin, votre coach IA et d'autres membres de la communauté",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun QuickActionsSection(navController: NavController) {
    Column {
        Text(
            text = "Actions rapides",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(getQuickActions()) { action ->
                QuickActionChip(action = action) {
                    handleQuickAction(navController, action.action)
                }
            }
        }
    }
}

@Composable
fun QuickActionChip(
    action: QuickAction,
    onClick: () -> Unit
) {
    ElevatedButton(
        onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Icon(
            imageVector = action.icon,
            contentDescription = null,
            modifier = Modifier.size(size = 18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = action.title)
    }
}

@Composable
fun ChatOptionsSection(navController: NavController) {
    Column {
        Text(
            text = "Options de chat",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            getChatOptions().forEach { option ->
                ChatOptionCard(
                    chatOption = option,
                    onClick = {
                        handleChatOptionClick(navController, option.type)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ChatOptionCard(
    chatOption: ChatOption,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(
            containerColor = chatOption.backgroundColor
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = chatOption.icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = chatOption.iconColor
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chatOption.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = chatOption.textColor
                )
                Text(
                    text = chatOption.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = chatOption.textColor.copy(alpha = 0.7f)
                )
            }

            if (chatOption.hasNotification) {
                Badge(
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Text(
                        text = chatOption.notificationCount.toString(),
                        color = MaterialTheme.colorScheme.onError
                    )
                }
            }
        }
    }
}

@Composable
fun RecentConversationsSection(navController: NavController) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Conversations récentes",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            TextButton(onClick = { /* TODO: Navigate to all conversations */ }) {
                Text("Voir tout")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            getRecentConversations().forEach { conversation ->
                RecentConversationItem(
                    conversation = conversation,
                    onClick = {
                        // TODO: Navigate to specific conversation
                    }
                )
            }
        }
    }
}

@Composable
fun RecentConversationItem(
    conversation: RecentConversation,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = conversation.participantIcon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = conversation.participantName,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = conversation.lastMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = conversation.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (conversation.unreadCount > 0) {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = conversation.unreadCount.toString(),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CommunityStatsSection() {
    Column {
        Text(
            text = "Statistiques de la communauté",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        val data = getCommunityStats()

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(data) { stat ->
                StatCard(stat = stat)
            }
        }
    }
}

@Composable
fun StatCard(stat: CommunityStat) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = stat.backgroundColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = stat.icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = stat.iconColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stat.value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = stat.textColor
            )
            Text(
                text = stat.label,
                style = MaterialTheme.typography.bodySmall,
                color = stat.textColor.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

// Data classes
data class QuickAction(
    val title: String,
    val icon: ImageVector,
    val action: String
)

data class ChatOption(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val type: ChatType,
    val backgroundColor: androidx.compose.ui.graphics.Color,
    val iconColor: androidx.compose.ui.graphics.Color,
    val textColor: androidx.compose.ui.graphics.Color,
    val hasNotification: Boolean = false,
    val notificationCount: Int = 0
)

data class RecentConversation(
    val participantName: String,
    val participantIcon: ImageVector,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0
)

data class CommunityStat(
    val label: String,
    val value: String,
    val icon: ImageVector,
    val backgroundColor: androidx.compose.ui.graphics.Color,
    val iconColor: androidx.compose.ui.graphics.Color,
    val textColor: androidx.compose.ui.graphics.Color
)

enum class ChatType {
    AI_COACH,
    DOCTOR,
    COMMUNITY_GROUP
}

// Utility functions
private fun getQuickActions(): List<QuickAction> {
    return listOf(
        QuickAction("Rendez-vous", Icons.Default.DateRange, "appointment"),
        QuickAction("Urgence", Icons.Default.Call, "emergency"),
        QuickAction("Rechercher", Icons.Default.Search, "search"),
        QuickAction("Notifications", Icons.Default.Notifications, "notifications")
    )
}

@Composable
private fun getChatOptions(): List<ChatOption> {
    return listOf(
        ChatOption(
            title = "Coach IA Nutritionnel",
            description = "Conseils personnalisés et suivi alimentaire 24/7",
            icon = Icons.Default.Face,
            type = ChatType.AI_COACH,
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            iconColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        ChatOption(
            title = "Médecin Traitant",
            description = "Consultations et suivi médical personnalisé",
            icon = Icons.Default.Info,
            type = ChatType.DOCTOR,
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            iconColor = MaterialTheme.colorScheme.secondary,
            textColor = MaterialTheme.colorScheme.onSecondaryContainer,
            hasNotification = true,
            notificationCount = 2
        ),
        ChatOption(
            title = "Communauté MealMate",
            description = "Échangez avec d'autres membres de la communauté",
            icon = Icons.Default.Favorite,
            type = ChatType.COMMUNITY_GROUP,
            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
            iconColor = MaterialTheme.colorScheme.tertiary,
            textColor = MaterialTheme.colorScheme.onTertiaryContainer,
            hasNotification = true,
            notificationCount = 5
        )
    )
}

private fun getRecentConversations(): List<RecentConversation> {
    return listOf(
        RecentConversation(
            participantName = "Dr. Abada Christian",
            participantIcon = Icons.Default.AddCircle,
            lastMessage = "Votre dernier bilan sanguin est excellent !",
            timestamp = "14:30",
            unreadCount = 1
        ),
        RecentConversation(
            participantName = "Coach IA",
            participantIcon = Icons.Default.Face,
            lastMessage = "Voulez-vous que je vous propose un nouveau plan alimentaire ?",
            timestamp = "12:15"
        ),
        RecentConversation(
            participantName = "Groupe Diabète",
            participantIcon = Icons.Default.Home,
            lastMessage = "Marie: Merci pour vos conseils concernant...",
            timestamp = "10:45",
            unreadCount = 3
        )
    )
}

@Composable
private fun getCommunityStats(): List<CommunityStat> {
    return listOf(
        CommunityStat(
            label = "Membres actifs",
            value = "1,247",
            icon = Icons.Default.Person,
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            iconColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        CommunityStat(
            label = "Messages aujourd'hui",
            value = "342",
            icon = Icons.Default.Send,
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            iconColor = MaterialTheme.colorScheme.secondary,
            textColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        CommunityStat(
            label = "Consultations",
            value = "28",
            icon = Icons.Default.DateRange,
            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
            iconColor = MaterialTheme.colorScheme.tertiary,
            textColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    )
}

private fun handleQuickAction(navController: NavController, action: String) {
    when (action) {
        "appointment" -> {
            // TODO: Navigate to appointment screen
        }
        "emergency" -> {
            // TODO: Navigate to emergency contact
        }
        "search" -> {
            navController.navigate("search")
        }
        "notifications" -> {
            navController.navigate("notifications")
        }
    }
}

private fun handleChatOptionClick(navController: NavController, chatType: ChatType) {
    when (chatType) {
        ChatType.AI_COACH -> {
            navController.navigate(Routes.Screen.ChatWithAIScreen.route) {
                popUpTo(Routes.Screen.CommunityScreen.route) {
                    saveState = false
                }
                launchSingleTop = true
            }
        }
        ChatType.DOCTOR -> {
            navController.navigate("chat_with_doctor") {
                popUpTo(Routes.Screen.CommunityScreen.route) {
                    saveState = false
                }
                launchSingleTop = true
            }
        }
        ChatType.COMMUNITY_GROUP -> {
            navController.navigate("community_chat") {
                popUpTo(Routes.Screen.CommunityScreen.route) {
                    saveState = false
                }
                launchSingleTop = true
            }
        }
    }
}