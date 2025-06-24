package com.example.mealmate.ui.view.settingsScreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mealmate.R
import com.example.mealmate.data.source.model.User
import com.example.mealmate.ui.view.components.myBottomAppBar.MyBottomAppBar
import com.example.mealmate.ui.view.components.myBottomAppBar.MyNavigationOnRail
import com.example.mealmate.ui.view.components.myTopAppBar.MyTopBar
import com.example.mealmate.ui.view.settingsScreen.components.ContenairSettingsItems
import com.example.mealmate.ui.view.components.rowIconText.RowContentItem

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SettingsScreen(
    navController: NavController,
    currentUser: User? = null,
    onLogout: () -> Unit
) {
    val user = currentUser?: return

    val context = LocalContext.current

    // États pour les dialogues et les préférences
    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var currentTheme by remember { mutableStateOf("Automatique") }
    var currentLanguage by remember { mutableStateOf("Français") }

    val settingsItems = listOf(
        // Section Profil
        listOf(
            RowContentItem(
                title = "Profil utilisateur",
                value = "Mes informations personnelles",
                icon = R.drawable.lock_filled_icn,
                onClick = {
                    // Navigation vers la page de profil
                    navController.navigate("profile")
                }
            ),
            RowContentItem(
                title = "Compte",
                value = "Gérer mon compte",
                icon = R.drawable.person_filled_icn,
                onClick = {
                    navController.navigate("account_management")
                }
            )
        ),

        // Section Sécurité
        listOf(
            RowContentItem(
                title = "Sécurité",
                value = "Changer de mot de passe",
                icon = R.drawable.lock_filled_icn,
                onClick = {
                    navController.navigate("change_password")
                }
            ),
            RowContentItem(
                title = "Authentification",
                value = "Authentification à deux facteurs",
                icon = R.drawable.security_icn,
                onClick = {
                    navController.navigate("two_factor_auth")
                }
            ),
            RowContentItem(
                title = "Sessions",
                value = "Gérer les appareils connectés",
                icon = R.drawable.device_outlined_icn,
                onClick = {
                    navController.navigate("active_sessions")
                }
            )
        ),

        // Section Préférences
        listOf(
            RowContentItem(
                title = "Apparence",
                value = currentTheme,
                icon = R.drawable.palette_icn,
                onClick = {
                    showThemeDialog = true
                }
            ),
            RowContentItem(
                title = "Langue",
                value = currentLanguage,
                icon = R.drawable.language_icn,
                onClick = {
                    showLanguageDialog = true
                }
            ),
            RowContentItem(
                title = "Notifications",
                value = if (notificationsEnabled) "Activées" else "Désactivées",
                icon = R.drawable.notifications_active_filled_icn,
                onClick = {
                    notificationsEnabled = !notificationsEnabled
                }
            )
        ),

        // Section Données et Confidentialité
        listOf(
            RowContentItem(
                title = "Données",
                value = "Gérer mes données",
                icon = R.drawable.data_usage_filled_icn,
                onClick = {
                    //navController.navigate("data_management")
                }
            ),
            RowContentItem(
                title = "Confidentialité",
                value = "Paramètres de confidentialité",
                icon = R.drawable.privacy_filled_icn,
                onClick = {
                    //navController.navigate("privacy_settings")
                }
            ),
            RowContentItem(
                title = "Stockage",
                value = "Cache et stockage local",
                icon = R.drawable.storage_filled_icn,
                onClick = {
                   // navController.navigate("storage_settings")
                }
            )
        ),

        // Section Social et Support
        listOf(
            RowContentItem(
                title = "Partage",
                value = "Inviter des amis",
                icon = R.drawable.share_filled_icn,
                onClick = {
                    shareApp(context)
                }
            ),
            RowContentItem(
                title = "Support",
                value = "Centre d'aide",
                icon = R.drawable.help_filled_icn,
                onClick = {
                    //navController.navigate("help_center")
                }
            ),
            RowContentItem(
                title = "Commentaires",
                value = "Donner votre avis",
                icon = R.drawable.feedback_filled_icn,
                onClick = {
                    openAppInPlayStore(context)
                }
            )
        ),

        // Section Légal et Déconnexion
        listOf(
            RowContentItem(
                title = "Légal",
                value = "Politique de confidentialité",
                icon = R.drawable.police_filled_icn,
                onClick = {
                    //navController.navigate("privacy_policy")
                }
            ),
            RowContentItem(
                title = "Conditions",
                value = "Termes et conditions",
                icon = R.drawable.description_filled_icn,
                onClick = {
                    //navController.navigate("terms_conditions")
                }
            ),
            RowContentItem(
                title = "À propos",
                value = "Version ${getAppVersion(context)}",
                icon = R.drawable.info_filled_icn,
                onClick = {
                    //navController.navigate("about")
                }
            ),
            RowContentItem(
                title = "",
                value = "Déconnexion",
                icon = R.drawable.logout_icn,
                onClick = {
                    showLogoutDialog = true
                }
            )
        )
    )


    BoxWithConstraints(modifier = Modifier.fillMaxSize()){
        if(maxWidth > 600.dp){
            Row(modifier = Modifier.fillMaxSize()){
                MyNavigationOnRail(navController = navController)

                // Contenu principal de l'écran
                BoxWithConstraints(modifier = Modifier.fillMaxSize())
                {
                    val columnCount = when {
                        maxWidth > 1200.dp -> 4
                        else -> 2
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(columnCount),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(settingsItems.size) { index ->
                            val cardItems = settingsItems[index]
                            ContenairSettingsItems(
                                listRowContent = cardItems,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }else{
            Scaffold(
                topBar = {
                    MyTopBar(
                        user = user,
                        modifier = Modifier,
                        onNotificationClick = { /*--Rien pour le moment--*/ },
                        onSearchClick = { /*--Rien pour le moment--*/  }
                    )
                },
                bottomBar = {
                    MyBottomAppBar(navController = navController)
                }
            ) { paddingValues ->
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(settingsItems.size) { index ->  // Utilisation de .size et index
                        val cardItems = settingsItems[index]
                        ContenairSettingsItems(
                            listRowContent = cardItems,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        // Dialogues de confirmation et de sélection
        if (showThemeDialog) {
            ThemeSelectionDialog(
                currentTheme = currentTheme,
                onThemeSelected = { theme ->
                    currentTheme = theme
                    showThemeDialog = false
                    // Appliquer le thème sélectionné
                    applyTheme(context, theme)
                },
                onDismiss = { showThemeDialog = false }
            )
        }

        if (showLanguageDialog) {
            LanguageSelectionDialog(
                currentLanguage = currentLanguage,
                onLanguageSelected = { language ->
                    currentLanguage = language
                    showLanguageDialog = false
                    // Appliquer la langue sélectionnée
                    applyLanguage(context, language)
                },
                onDismiss = { showLanguageDialog = false }
            )
        }

        if (showLogoutDialog) {
            LogoutConfirmationDialog(
                onConfirm = {
                    showLogoutDialog = false
                    onLogout()
                },
                onDismiss = { showLogoutDialog = false }
            )
        }
    }
}


@Composable
fun ThemeSelectionDialog(
    currentTheme: String,
    onThemeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val themes = listOf("Automatique", "Clair", "Sombre")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choisir le thème") },
        text = {
            Column {
                themes.forEach { theme ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onThemeSelected(theme) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentTheme == theme,
                            onClick = { onThemeSelected(theme) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(theme)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fermer")
            }
        }
    )
}

@Composable
fun LanguageSelectionDialog(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val languages = listOf("Français", "English", "Español", "Deutsch")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choisir la langue") },
        text = {
            Column {
                languages.forEach { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageSelected(language) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentLanguage == language,
                            onClick = { onLanguageSelected(language) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(language)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fermer")
            }
        }
    )
}

@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Déconnexion") },
        text = { Text("Êtes-vous sûr de vouloir vous déconnecter ?") },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Déconnexion")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}

// Fonctions utilitaires
private fun shareApp(context: Context) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, "Découvrez cette application géniale : [Lien pas encore dispo]")
        putExtra(Intent.EXTRA_SUBJECT, "Recommandation d'application")
    }
    context.startActivity(Intent.createChooser(shareIntent, "Partager l'application"))
}

private fun openAppInPlayStore(context: Context) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${context.packageName}"))
        context.startActivity(intent)
    } catch (e: Exception) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}"))
        context.startActivity(intent)
    }
}

private fun getAppVersion(context: Context): String {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName ?: "1.0.0"
    } catch (e: Exception) {
        "1.0.0"
    }
}

private fun applyTheme(context: Context, theme: String) {
    // Implémentation pour appliquer le thème
    // Vous pouvez utiliser SharedPreferences ou DataStore pour sauvegarder le choix
}

private fun applyLanguage(context: Context, language: String) {
    // Implémentation pour changer la langue
    // Utiliser les ressources Android pour la localisation
}