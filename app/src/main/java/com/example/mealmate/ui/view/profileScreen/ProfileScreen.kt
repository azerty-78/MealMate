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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mealmate.R
import com.example.mealmate.data.source.model.MedicalParameters
import com.example.mealmate.data.source.model.User
import com.example.mealmate.ui.view.components.bottomSheet.RowIconBtmSheet
import com.example.mealmate.ui.view.components.myBottomAppBar.MyBottomAppBar
import com.example.mealmate.ui.view.components.myBottomAppBar.MyNavigationOnRail
import com.example.mealmate.ui.view.components.myTopAppBar.MyTopBar
import com.example.mealmate.ui.view.components.rowIconText.RowContentItem
import com.example.mealmate.ui.view.profileScreen.component.ContenairProfileItem
import com.example.mealmate.ui.view.profileScreen.component.DatePickerBottomSheet
import kotlin.toString

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ProfileScreen(
    navController: NavController,
    currentUser: User? = null,
    viewModel: MedicalParametersViewModel = hiltViewModel()
) {
    val user = currentUser?: return
    val medicalState by viewModel.uiState

    // Observez les changements de paramètres médicaux
    LaunchedEffect(Unit) {
        viewModel.getMedicalParameters()
    }

    var showDatePicker by remember { mutableStateOf(false) }

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
                onClick = { } // non modifiable
            )
        ),

        listOf(
            RowContentItem(
                title = "Taille",
                value = medicalState.currentParameters?.height?.toString() ?: "Entrez votre taille (m)",
                icon = R.drawable.person_filled_icn,//height_icn,
                keyboardType = KeyboardType.Decimal,
                onClick = onRowClick
            ),
            RowContentItem(
                title = "Poids initial",
                value = medicalState.currentParameters?.initialWeight?.toString() ?: "Entrez votre poids initial (Kg)",
                icon = R.drawable.person_filled_icn,//weight_icn,
                keyboardType = KeyboardType.Decimal,
                onClick = onRowClick
            ),
            RowContentItem(
                title = "Poids actuel",
                value = medicalState.currentParameters?.currentWeight?.toString() ?: "Entrez votre poids actuel (Kg)",
                icon = R.drawable.person_filled_icn,//weight_icn,
                keyboardType = KeyboardType.Decimal,
                onClick = onRowClick
            ),
            RowContentItem(
                title = "Groupe sanguin",
                value = medicalState.currentParameters?.bloodType ?: "Entrez votre groupe sanguin",
                icon = R.drawable.person_filled_icn,//blood_icn,
                onClick = onRowClick
            ),
            RowContentItem(
                title = "Date de naissance",
                value = (medicalState.currentParameters?.birthDate ?: "jj/MM/aaaa").toString(),
                icon = R.drawable.person_filled_icn,//calendar_icn,
                onClick = { item ->
                    bottomSheetItem = item
                    // Ici vous pourriez ouvrir un DatePicker au lieu d'un champ texte
                    isBottomSheetVisible = true
                }
            )
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
                    // Mettre à jour les paramètres médicaux
                    val currentParams = medicalState.currentParameters ?: MedicalParameters(userId = user.id.toString())

                    val updatedParams = when(bottomSheetItem?.title) {
                        "Taille" -> currentParams.copy(height = newValue.toDoubleOrNull())
                        "Poids initial" -> currentParams.copy(initialWeight = newValue.toDoubleOrNull())
                        "Poids actuel" -> currentParams.copy(currentWeight = newValue.toDoubleOrNull())
                        "Groupe sanguin" -> currentParams.copy(bloodType = newValue)
                        "Date de naissance" -> currentParams.copy(birthDate = newValue)
                        else -> currentParams
                    }

                    viewModel.createOrUpdateMedicalParameters(updatedParams)
                    isBottomSheetVisible = false
                    bottomSheetItem = null // Réinitialiser l'item sélectionné
                },
                onValueChange = { valToUpdate ->
                    bottomSheetItem = bottomSheetItem?.copy(value = valToUpdate)
                }
            )
        }



        // Plus bas dans votre UI :
        if (showDatePicker) {
            DatePickerBottomSheet(
                isVisible = true,
                onDismiss = { showDatePicker = false },
                onDateSelected = { dateStr ->
                    val currentParams = medicalState.currentParameters ?: MedicalParameters(userId = user.id.toString())
                    viewModel.createOrUpdateMedicalParameters(currentParams.copy(birthDate = dateStr))
                }
            )
        }
    }
}