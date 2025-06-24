package com.example.mealmate.ui.view.dashboardScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mealmate.data.source.model.User
import com.example.mealmate.ui.view.components.myBottomAppBar.MyBottomAppBar
import com.example.mealmate.ui.view.components.myBottomAppBar.MyNavigationOnRail
import com.example.mealmate.ui.view.components.myTopAppBar.MyTopBar
import com.example.mealmate.ui.view.dashboardScreen.component.AlertsCard
import com.example.mealmate.ui.view.dashboardScreen.component.CameroonianDiabetesData
import com.example.mealmate.ui.view.dashboardScreen.component.GlycemiaChartCard
import com.example.mealmate.ui.view.dashboardScreen.component.GlycemiaDetailsDialog
import com.example.mealmate.ui.view.dashboardScreen.component.GlycemiaReading
import com.example.mealmate.ui.view.dashboardScreen.component.GlycemiaStatus
import com.example.mealmate.ui.view.dashboardScreen.component.HeaderSection
import com.example.mealmate.ui.view.dashboardScreen.component.MealDetailsDialog
import com.example.mealmate.ui.view.dashboardScreen.component.MealEntry
import com.example.mealmate.ui.view.dashboardScreen.component.MealsSection
import com.example.mealmate.ui.view.dashboardScreen.component.MedicationsCard
import com.example.mealmate.ui.view.dashboardScreen.component.NutritionRecommendationsCard
import com.example.mealmate.ui.view.dashboardScreen.component.NutritionSummaryDialog
import com.example.mealmate.ui.view.dashboardScreen.component.QuickStatsSection
import com.example.mealmate.ui.view.dashboardScreen.component.WeeklyTrendsCard
import java.time.LocalTime

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DashboardScreen(
    navController: NavController,
    currentUser: User? = null
) {
    val user = currentUser?: return
    var selectedPeriod by remember { mutableStateOf("Aujourd'hui") }
    var showGlycemiaDialog by remember { mutableStateOf(false) }
    var showMealDialog by remember { mutableStateOf(false) }
    var showNutritionDialog by remember { mutableStateOf(false) }
    var selectedMeal by remember { mutableStateOf<MealEntry?>(null) }

    val data = CameroonianDiabetesData
    val lastGlycemia = data.sampleGlycemiaData.lastOrNull() ?:
    GlycemiaReading(LocalTime.now(), 120, "Actuel", GlycemiaStatus.NORMAL)


    BoxWithConstraints(modifier = Modifier.fillMaxSize()){
        if(maxWidth > 600.dp){
            Row(modifier = Modifier.fillMaxSize()){
                MyNavigationOnRail(navController = navController)

                // Contenu principal de l'écran
                BoxWithConstraints(modifier = Modifier.fillMaxSize())
                {
                    //
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header avec sélection de période
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        HeaderSection(
                            selectedPeriod = selectedPeriod,
                            onPeriodSelected = { selectedPeriod = it }
                        )
                    }

                    // Cartes de statistiques rapides
                    item {
                        QuickStatsSection(
                            lastGlycemia = lastGlycemia,
                            onGlycemiaClick = { showGlycemiaDialog = true },
                            onNutritionClick = { showNutritionDialog = true }
                        )
                    }

                    // Graphique de glycémie
                    item {
                        GlycemiaChartCard(
                            data = data.sampleGlycemiaData,
                            onDetailsClick = { showGlycemiaDialog = true }
                        )
                    }

                    // Repas du jour
                    item {
                        MealsSection(
                            meals = data.sampleMeals,
                            onMealClick = { meal ->
                                selectedMeal = meal
                                showMealDialog = true
                            }
                        )
                    }

                    // Tendances hebdomadaires
                    item {
                        WeeklyTrendsCard(trends = data.weeklyTrends)
                    }

                    // Médicaments et alertes
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MedicationsCard(
                                medications = data.medications,
                                modifier = Modifier.weight(1f)
                            )
                            AlertsCard(
                                alerts = data.alerts,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Recommandations nutritionnelles
                    item {
                        NutritionRecommendationsCard(dishes = data.dishes)
                    }
                }

                // Dialogs
                if (showGlycemiaDialog) {
                    GlycemiaDetailsDialog(
                        data = data.sampleGlycemiaData,
                        onDismiss = { showGlycemiaDialog = false }
                    )
                }

                if (showMealDialog && selectedMeal != null) {
                    MealDetailsDialog(
                        meal = selectedMeal!!,
                        onDismiss = {
                            showMealDialog = false
                            selectedMeal = null
                        }
                    )
                }

                if (showNutritionDialog) {
                    NutritionSummaryDialog(
                        meals = data.sampleMeals,
                        onDismiss = { showNutritionDialog = false }
                    )
                }
            }
        }
    }
}