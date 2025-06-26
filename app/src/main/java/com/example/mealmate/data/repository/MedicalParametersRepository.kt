package com.example.mealmate.data.repository


import com.example.mealmate.data.source.model.MedicalParameters
import com.example.mealmate.util.Listener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MedicalParametersRepository
@Inject constructor() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun createOrUpdateMedicalInfo(medicalInfo: MedicalParameters) = flow {
        emit(Listener.Loading)
        val userId = getCurrentUserId()
        if (userId == null) {
            emit(Listener.Error(e = Exception("User not logged in"), message = "Utilisateur non connecté"))
            return@flow
        }

        // Assurez-vous que medicalInfo contient le bon userId
        val infoToSave = medicalInfo.copy(userId = userId)

        firestore.collection("medical_info")
            .document(userId)
            .set(infoToSave)
            .await()
        emit(Listener.Success(Unit))
    }.catch { e ->
        emit(Listener.Error(e = e as Exception, message = e.message ?: "Erreur lors de la sauvegarde des informations médicales"))
    }

    fun getMedicalInfo() = flow {
        emit(Listener.Loading)
        val userId = getCurrentUserId()
        if (userId == null) {
            emit(Listener.Success(null))
            return@flow
        }

        val documentSnapshot = firestore.collection("medical_info")
            .document(userId)
            .get()
            .await()

        if (documentSnapshot.exists()) {
            val medicalInfo = documentSnapshot.toObject(MedicalParameters::class.java)
            emit(Listener.Success(medicalInfo))
        } else {
            emit(Listener.Success(null)) // Aucune information médicale trouvée pour cet utilisateur
        }
    }.catch { e ->
        emit(Listener.Error(e = e as Exception, message = e.message ?: "Erreur lors de la récupération des informations médicales"))
    }

}