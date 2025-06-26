package com.example.mealmate.data.repository

import android.content.Intent
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.example.mealmate.data.customModel.GoogleAuthUiHelper
import com.example.mealmate.data.source.model.SignInResult
import com.example.mealmate.data.source.model.User
import com.example.mealmate.util.ConnectivityObserver
import com.example.mealmate.util.Listener
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UserRepository
@Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val googleAuthUiHelper: GoogleAuthUiHelper,
    private val connectivityObserver: ConnectivityObserver
){

    private suspend fun saveLoginState(isLoggedIn: Boolean) {
        dataStore.edit { preferences->
            preferences[PreferencesKeys.IS_LOGGED_IN] = isLoggedIn
        }
    }

    fun signUp(email: String, password: String) = flow{
        //etat de chargement
        emit(Listener.Loading)

        //processus de connexion
        val authResult = suspendCancellableCoroutine<AuthResult> { next->
            val auth = Firebase.auth
            val task = auth.createUserWithEmailAndPassword(email, password)

            //gestion du succes
            task.addOnCompleteListener { taskResult->
                if(next.isCancelled) return@addOnCompleteListener
                if(taskResult.isSuccessful) next.resume(task.result!!)
            }

            //gestion de l'erreur
            task.addOnFailureListener { exception->
                if(next.isCancelled) return@addOnFailureListener
                next.resumeWithException(exception)
            }
        }

        //etat de succes
        emit(Listener.Success(authResult))
    }.catch { excep->
        emit(Listener.Error(
            e = excep as Exception,
            message = excep.message
        ))
    }

    fun signIn(email: String, password: String) = flow{
        if (!connectivityObserver.isOnline()) {
            emit(Listener.Error(e = Exception("No internet connection"), message = "Aucune connexion internet"))
            return@flow
        }
        //emission sequentielle de l'état de chargement
        //etat de chargement
        emit(Listener.Loading)


        //processus de connexion
        val authResult = suspendCancellableCoroutine<AuthResult> { next->
            val auth = Firebase.auth
            val task = auth.signInWithEmailAndPassword(email, password)

            //gestion du succes
            task.addOnCompleteListener { taskResult->
                if(next.isCancelled) return@addOnCompleteListener
                if(taskResult.isSuccessful) next.resume(task.result!!)
            }

            //gestion de l'erreur
            task.addOnFailureListener { exception->
                if(next.isCancelled) return@addOnFailureListener
                next.resumeWithException(exception)
            }

        }
        saveLoginState(true) // Sauvegarde l'état de connexion
        //etat de succes
        emit(Listener.Success(authResult))
    }.catch { excep->
        if(excep is FirebaseAuthInvalidCredentialsException){
            emit(Listener.Error(e = excep, message = "Email ou mot de passe incorrect"))
            return@catch
        }
        if(excep is FirebaseAuthInvalidUserException){
            emit(Listener.Error(e = excep, message = "Email incorrect"))
            return@catch
        }
        emit(Listener.Error(
            e = excep as Exception,
            message = excep.message
        ))
    }

    fun logOut(oneTapClient: SignInClient? = null) = flow {
        emit(Listener.Loading)

        val auth = Firebase.auth
        if(oneTapClient != null) oneTapClient.signOut().await()
        auth.signOut()

        saveLoginState(false) // Met à jour l'état de déconnexion
        emit(Listener.Success(Unit))
    }.catch { excep->
        emit(Listener.Error(
            e = excep as Exception,
            message = excep.message
        ))
    }

    fun getSignedUser() = flow {
        emit(Listener.Loading)

        val user = Firebase.auth.currentUser
        val signedUser = if(user!=null){
            User(
                id = user.uid,
                email = user.email.toString(),
                username = user.displayName.toString(),
                profilePictureUrl = user.photoUrl.toString()
            )
        }else null

        emit(Listener.Success(data = signedUser))
    }.catch { excep->
        emit(Listener.Error(
            e = excep as Exception,
            message = excep.message
        ))
    }

    suspend fun signInWithGoogle(intent: Intent?): SignInResult {
        return try {
            val credential = googleAuthUiHelper.getGoogleCredential(intent)
            val googleIdToken = credential.googleIdToken
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

            val authResult = Firebase.auth.signInWithCredential(firebaseCredential).await()
            val user = authResult.user ?: throw Exception("Google sign-in failed")

            saveLoginState(true)

            SignInResult(
                data = User(
                    id = user.uid,
                    email = user.email ?: "",
                    username = user.displayName,
                    profilePictureUrl = user.photoUrl?.toString()
                ),
                errorMessage = null
            )
        } catch (e: Exception) {
            SignInResult(null, e.message)
        }
    }

    fun updateProfile(username: String?, photoUri: Uri?) = flow {
        emit(Listener.Loading)

        val firebaseStorage = FirebaseStorage.getInstance()
        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            emit(
                Listener.Error(
                    e = Exception("User not logged in"),
                    message = "Utilisateur non connecté"
                )
            )
            return@flow
        }

        var photoUrlString: String? = currentUser.photoUrl?.toString()

        // 1. Mettre à jour la photo de profil si une nouvelle URI est fournie
        if (photoUri != null) {
            // Créez une référence à 'profile_pictures/userId/imageName.jpg'
            val photoRef = firebaseStorage.reference.child("profile_pictures/${currentUser.uid}/${UUID.randomUUID()}.jpg")
            // Téléchargez le fichier
            photoRef.putFile(photoUri).await()
            // Obtenez l'URL de téléchargement
            photoUrlString = photoRef.downloadUrl.await().toString()
        }

        // 2. Construire la requête de mise à jour du profil
        val profileUpdates = UserProfileChangeRequest.Builder()
        if (username != null) {
            profileUpdates.displayName = username
        }
        if (photoUrlString != null) {
            profileUpdates.photoUri = Uri.parse(photoUrlString)
        }

        // 3. Mettre à jour le profil utilisateur Firebase
        currentUser.updateProfile(profileUpdates.build()).await()


        // 4. Récupérer l'utilisateur mis à jour
        val updatedUser = User(
            id = currentUser.uid,
            email = currentUser.email ?: "",
            username = currentUser.displayName,
            profilePictureUrl = currentUser.photoUrl?.toString()
        )
        emit(Listener.Success(updatedUser))
    }.catch { excep->
        emit(Listener.Error(
            e = excep as Exception,
            message = excep.message //"Erreur lors de la mise à jour du profil"
        ))
    }


    // Fonction pour obtenir l'ID de l'utilisateur actuellement connecté
    fun getCurrentUserId(): String? {
        val firebaseAuth = Firebase.auth
        return firebaseAuth.currentUser?.uid
    }
}

object PreferencesKeys {
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
}