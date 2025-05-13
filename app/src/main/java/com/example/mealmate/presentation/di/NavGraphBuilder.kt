package com.example.mealmate.presentation.di

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.mealmate.presentation.navgraph.Routes
import com.example.mealmate.ui.view.authentification.ForgotPasswordScreen
import com.example.mealmate.ui.view.authentification.SignUpScreen
import com.example.mealmate.ui.view.authentification.SingInScreen
import com.example.mealmate.ui.view.authentification.state.Auth_event
import com.example.mealmate.ui.view.authentification.state.Auth_viewModel


//authentification graph
fun NavGraphBuilder.authGraph(navController: NavController){
    navigation(
        startDestination = Routes.Screen.SignInScreen.route,
        route = Routes.AUTH_GRAPHROUTE
    ){
        composable(route = Routes.Screen.SignInScreen.route){
            val viewModel: Auth_viewModel = hiltViewModel()

            // Gérer la navigation après connexion réussie
            LaunchedEffect(viewModel.uiState.value.successSignIn) {
                if (viewModel.uiState.value.successSignIn) {
                    navController.navigate(Routes.HOME_GRAPHROUTE) {
                        popUpTo(Routes.AUTH_GRAPHROUTE) { inclusive = true }
                    }
                }
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        viewModel.onEvent(Auth_event.OnGoogleSignInResult(result.data))
                    }
                }
            )

            SingInScreen(
                navController = navController,
                onSignUpClick = {
                    navController.navigate(Routes.Screen.SingUpScreen.route) {
                        popUpTo(Routes.Screen.SingUpScreen.route) { inclusive = true }
                    }
                },
                onForgotPwdClick = {
                    navController.navigate(Routes.Screen.ForgotPasswordScreen.route){
                        popUpTo(Routes.Screen.ForgotPasswordScreen.route) { inclusive = true }
                    }
                },
                onSignInSuccess = {
//                    navController.navigate(Routes.HOME_GRAPHROUTE){
//                        popUpTo(Routes.AUTH_GRAPHROUTE)
//                    }
                },
                onSignInWithGoogle = {
                    viewModel.onEvent(Auth_event.OnSignInWithGoogle { intentSender ->
                        launcher.launch(IntentSenderRequest.Builder(intentSender).build())
                    })
                }
            )
        }
        composable(Routes.Screen.SingUpScreen.route){
            SignUpScreen(
                navController = navController,
                onSignInClick = {
                    navController.navigate(Routes.Screen.SignInScreen.route) {
                        popUpTo(Routes.Screen.SignInScreen.route) { inclusive = true }
                    }
                },
                onSignUpSuccess = {
                    navController.navigate(Routes.Screen.SignInScreen.route){
                        popUpTo(Routes.AUTH_GRAPHROUTE){ inclusive = true }
                    }
                }
            )
        }
        composable(Routes.Screen.ForgotPasswordScreen.route){
            ForgotPasswordScreen(navController = navController)
        }
    }
}