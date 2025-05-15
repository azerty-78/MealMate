package com.example.mealmate.ui.view.authentification.state

import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmate.data.repository.UserRepository
import com.google.android.gms.auth.api.identity.SignInClient
import com.example.mealmate.data.customModel.GoogleAuthUiHelper
import com.example.mealmate.util.ConnectivityObserver
import com.example.mealmate.util.Listener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Auth_viewModel
@Inject constructor(
    private val userRep: UserRepository,
    private val oneTapClient: SignInClient,
    private val googleAuthUiHelper: GoogleAuthUiHelper,
    val connectivityObserver: ConnectivityObserver
) : ViewModel()
{
    val uiState = mutableStateOf(Auth_state())
    val networkStatus = mutableStateOf(ConnectivityObserver.Status.Neutral)

    companion object{
        private const val TAG = "Auth_viewModel"
    }

    init {
        getSignedUser()
        observeConnectivity()
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                networkStatus.value = status
                Log.e(TAG, "Network status : ${networkStatus.value}")
            }
        }
    }

    fun onEvent(event: Auth_event){
        when(event){
            is Auth_event.OnSignIn -> {
                signIn(event.userEmail, event.userPassword)
            }
            is Auth_event.OnLogout -> {
                logout()
            }
            is Auth_event.OnSignUp -> {
                signUp(event.userEmail, event.userPassword)
            }

            is Auth_event.OnSignInWithGoogle -> {
                viewModelScope.launch {
                    try {
                        val intentSender = googleAuthUiHelper.getSignInIntent()
                        intentSender?.let {
                            event.onIntentSender(it)
                        }
                    } catch (e: Exception) {
                        uiState.value = uiState.value.copy(
                            errorSignIn = e.message,
                            isLoading = false
                        )
                    }
                }
            }

            is Auth_event.OnGoogleSignInResult -> {
                handleGoogleSignInResult(event.intent)
            }
        }
    }

    //SignIn
    fun signIn(email: String, password: String){
        viewModelScope.launch{
            userRep.signIn(email, password).collect{result->
                when(result){
                    is Listener.Loading -> {
                        uiState.value = uiState.value.copy(
                            isLoading = true,
                            successSignIn = false,
                            errorSignIn = null
                        )
                    }
                    is Listener.Success -> {
                        uiState.value = uiState.value.copy(
                            isLoading = false,
                            successSignIn = true,
                            //currentUser = result.data
                        )
                    }
                    is Listener.Error -> {
                        Log.e(TAG, "Sign-in error", result.e)
                        uiState.value = uiState.value.copy(
                            isLoading = false,
                            successSignIn = false,
                            errorSignIn = result.message
                        )
                    }
                }
            }
        }
    }

    //signUp
    fun signUp(email: String, password: String){
        viewModelScope.launch {
            userRep.signUp(email, password).collect{result->
                when(result){
                    Listener.Loading -> {
                        uiState.value = uiState.value.copy(
                            isLoading = true,
                            successSignUp = false,
                            errorSignUp = null
                        )
                    }
                    is Listener.Success -> {
                        uiState.value = uiState.value.copy(
                            isLoading = false,
                            successSignUp = true,
                            //currentUser = result.data
                        )
                    }
                    is Listener.Error -> {
                        Log.e(TAG, "Sign-up error", result.e)
                        uiState.value = uiState.value.copy(
                            isLoading = false,
                            successSignUp = false,
                            errorSignUp = result.message
                        )
                    }
                }
            }
        }
    }

    //getSignedUser
    fun getSignedUser(){
        viewModelScope.launch{
            userRep.getSignedUser().collect{result->
                when(result){
                    is Listener.Error -> {
                        Log.e(TAG, "Error getting signed user", result.e)
                        uiState.value = uiState.value.copy(
                            isLoading = false,
                            currentUser = null,
                            errorSignIn = result.message
                        )
                    }
                    is Listener.Loading -> {
                        uiState.value = uiState.value.copy(
                            isLoading = true
                        )
                    }
                    is Listener.Success -> {
                        uiState.value = uiState.value.copy(
                            isLoading = false,
                            currentUser = result.data
                        )
                    }
                }
            }
        }
    }

    //logout
    fun logout(){
        viewModelScope.launch {
            userRep.logOut(oneTapClient).collect { result ->
                when(result){
                    is Listener.Error -> {
                        Log.e(TAG, "Logout error", result.e)
                        uiState.value = uiState.value.copy(
                            isLoading = false,
                            currentUser = null,
                            errorSignIn = result.message
                        )
                    }
                    is Listener.Loading -> {
                        uiState.value = uiState.value.copy(
                            isLoading = true
                        )
                    }
                    is Listener.Success -> {
                        uiState.value = uiState.value.copy(
                            isLoading = false,
                            currentUser = null
                        )
                    }
                }
            }
        }
    }

    fun handleGoogleSignInResult(intent: Intent?) {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(
                isLoading = true,
                isGoogleSignIn = true
            )

            try {
                val result = userRep.signInWithGoogle(intent)
                if (result.data != null) {
                    uiState.value = uiState.value.copy(
                        currentUser = result.data,
                        successSignIn = true,
                        isGoogleSignIn = true,
                        isLoading = false
                    )
                } else {
                    Log.e(TAG, "handleGoogleSignInResult error : ${result.errorMessage}")
                    uiState.value = uiState.value.copy(
                        errorSignIn = result.errorMessage,
                        isGoogleSignIn = false,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in handleGoogleSignInResult", e)
                uiState.value = uiState.value.copy(
                    errorSignIn = e.message,
                    isGoogleSignIn = false,
                    isLoading = false
                )
            }
        }
    }
}