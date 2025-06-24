package com.example.mealmate.data.repository

import com.example.mealmate.ui.view.components.NewsItem.NewsData
import com.example.mealmate.util.ConnectivityObserver
import com.example.mealmate.util.Listener2
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.io.IOException
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val connectivityObserver: ConnectivityObserver
) {
     val firestoreDB =  FirebaseFirestore.getInstance()

    fun getNews() = flow<Listener2<List<NewsData>>> {
        emit(Listener2.Loading())

        // Vérifier la connexion internet
        if (!connectivityObserver.isOnline()) {
            throw IOException("Pas de connexion internet")
        }

        val snapshot = firestoreDB.collection("news")
            .orderBy("publicationDate", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .await()

        val newsList = snapshot.documents.mapNotNull { document ->
            try {
                document.toObject(NewsData::class.java)//?.copy(id = document.id)
            } catch (e: Exception) {
                null
            }
        }

        emit(Listener2.Success(newsList))
    }.catch { exception ->
        emit(Listener2.Error(
            e = exception,
            message = when (exception) {
                is IOException -> "Vérifiez votre connexion internet"
                is FirebaseFirestoreException -> "Erreur lors de la récupération des actualités"
                else -> "Une erreur inattendue est survenue"
            }
        ))
    }
}