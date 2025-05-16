package com.example.mealmate.ui.view.profileScreen

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mealmate.R
import com.example.mealmate.data.source.model.User
import com.example.mealmate.ui.view.components.bottomSheet.RowIconBtmSheet
import com.example.mealmate.ui.view.components.myBottomAppBar.MyBottomAppBar
import com.example.mealmate.ui.view.components.myBottomAppBar.MyNavigationOnRail
import com.example.mealmate.ui.view.components.myTopAppBar.MyTopBar
import com.example.mealmate.ui.view.components.rowIconText.RowContentItem
import com.example.mealmate.ui.view.profileScreen.component.ContenairProfileItem
import kotlin.toString

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ProfileScreen(
    navController: NavController,
    currentUser: User? = null,
) {
    val user = currentUser?: return

    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var bottomSheetItem by remember { mutableStateOf<RowContentItem?>(null) } // Pour stocker l'item cliqué

    val onRowClick: (RowContentItem) -> Unit = { item ->
        bottomSheetItem = item // Stocker l'item cliqué
        isBottomSheetVisible = true // Afficher le Bottom Sheet
    }

    val profileItems = listOf(
        listOf(
            RowContentItem(
                title = "Utilisateur", value = user.username.toString(), icon = R.drawable.person_filled_icn,
                onClick =  onRowClick
            ),
            RowContentItem(
                title = "Email", value = user.email.toString(), icon = R.drawable.person_filled_icn,
                onClick = { }
            ),
            RowContentItem(
                title = "Téléphone", value = user.password.toString(), icon = R.drawable.person_filled_icn,
                onClick = onRowClick
            ),
            RowContentItem(
                title = "Adresse", value = "Le monde", icon = R.drawable.person_filled_icn,
                onClick = onRowClick
            ),
        ),

        listOf(
            RowContentItem(
                title = "Paramètres", value = "Gérer les paramètres", icon = R.drawable.person_filled_icn,
                onClick = { /* Naviguer vers les paramètres */ }
            ),
            RowContentItem(
                title = "Notifications", value = "Voir les notifications", icon = R.drawable.person_filled_icn,
                onClick = { /* Naviguer vers les notifications */ }
            ),
            RowContentItem(
                title = "À propos", value = "Informations sur l'application", icon = R.drawable.person_filled_icn,
                onClick = { /* Afficher la page à propos */ }
            ),
            RowContentItem(
                title = "Déconnexion", value = "Se déconnecter", icon = R.drawable.person_filled_icn,
                onClick = { /* Déconnecter l'utilisateur */ }
            ),
        ),
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
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(profileItems.size) { index ->
                            val cardItems = profileItems[index]

                            ContenairProfileItem(
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
                BoxWithConstraints(modifier = Modifier.padding(paddingValues)) {
                    val columnCount = when {
                        maxWidth > 1200.dp -> 4// pc/web
                        maxWidth > 600.dp -> 2//tablette
                        else -> 1//smartphone
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(columnCount),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(profileItems.size) { index ->  // Utilisation de .size et index
                            val cardItems = profileItems[index]
                            ContenairProfileItem(
                                listRowContent = cardItems,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))


                }
            }
        }

        if (isBottomSheetVisible && bottomSheetItem != null){
            RowIconBtmSheet(
                title = "Modifier ${bottomSheetItem?.title ?: ""}",
                value = bottomSheetItem?.value,
                isVisible = isBottomSheetVisible,
                icon = bottomSheetItem?.icon ?: R.drawable.person_filled_icn,
                keyboardType = bottomSheetItem?.keyboardType ?: KeyboardType.Text,
                onDismiss = { isBottomSheetVisible = false },
                onConfirm = { newValue ->
                    // Mettre à jour la valeur dans le ViewModel ou effectuer l'action nécessaire
                    Log.d("Modifier ", "Nouvelle valeur pour ${bottomSheetItem?.title}: $newValue")
                    // myViewModel.updateUserInformation(bottomSheetItem?.key, newValue)
                    isBottomSheetVisible = false
                    bottomSheetItem = null // Réinitialiser l'item sélectionné
                },
                onValueChange = { /*****************/ }
            )
        }
    }
}