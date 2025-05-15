package com.example.mealmate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.mealmate.presentation.navgraph.authGraph
import com.example.mealmate.presentation.navgraph.Routes
import com.example.mealmate.presentation.navgraph.dashboardGraph

@Composable
fun MainScreen(
    navController: NavHostController
) {
    // Empêche la réinitialisation lors de la rotation
    DisposableEffect(Unit) {
        onDispose { }
    }
    NavHost(
        navController = navController,
        startDestination = Routes.AUTH_GRAPHROUTE,
    ){
        authGraph(navController = navController)
        //homeGraph(navController = navController)
        dashboardGraph(navController = navController)
        //communityGraph(navController = navController)
        //settingsGraph(navController = navController)
        //profileGraph(navController = navController)
    }
}