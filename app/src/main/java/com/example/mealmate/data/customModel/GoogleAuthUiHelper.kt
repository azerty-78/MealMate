package com.example.mealmate.data.customModel

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.example.mealmate.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import kotlinx.coroutines.tasks.await


class GoogleAuthUiHelper(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    suspend fun getSignInIntent(): IntentSender? {
        return try {
            oneTapClient.beginSignIn(
                BeginSignInRequest.builder()
                    .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                            .setSupported(true)
                            .setServerClientId(context.getString(R.string.client_id))
                            .setFilterByAuthorizedAccounts(false)
                            .build()
                    )
                    .build()
            ).await()?.pendingIntent?.intentSender
        } catch (e: Exception) {
            Log.e("GoogleAuth", "Erreur Google Sign-In", e)
            null
        }
    }

    suspend fun getGoogleCredential(intent: Intent?): SignInCredential {
        return oneTapClient.getSignInCredentialFromIntent(intent)
    }
}