package com.example.mealmate.ui.view.components.NewsItem

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmate.data.repository.NewsRepository
import com.example.mealmate.util.ConnectivityObserver
import com.example.mealmate.util.Listener2
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {
    // État du réseau
    private val _networkStatus = MutableStateFlow(ConnectivityObserver.Status.Neutral)
    val networkStatus: StateFlow<ConnectivityObserver.Status> = _networkStatus.asStateFlow()

    // État des actualités
    private val _newsState = MutableStateFlow<Listener2<List<NewsData>>>(Listener2.Loading())
    val newsState: StateFlow<Listener2<List<NewsData>>> = _newsState.asStateFlow()

    // État pour les messages toast
    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    init {
        observeConnectivity()
        fetchNews()
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                _networkStatus.value = status
                Log.d(TAG, "Network status: $status")

                if (status == ConnectivityObserver.Status.Unavailable) {
                    _toastMessage.emit("Connexion internet perdue")
                }
            }
        }
    }

    fun fetchNews() {
        viewModelScope.launch {
            _newsState.value = Listener2.Loading()

            newsRepository.getNews()
                .catch { e ->
                    val errorMessage = when (e) {
                        is IOException -> "Erreur de connexion. Vérifiez votre internet"
                        is FirebaseFirestoreException -> "Erreur lors de la récupération des actualités"
                        else -> "Une erreur inattendue est survenue: ${e.localizedMessage}"
                    }
                    _toastMessage.emit(errorMessage)
                    _newsState.value = Listener2.Error(e, errorMessage)
                }
                .collect { listener ->
                    _newsState.value = listener
                }
        }
    }

    fun refreshNews() {
        if (_networkStatus.value == ConnectivityObserver.Status.Available) {
            fetchNews()
        } else {
            viewModelScope.launch {
                _toastMessage.emit("Pas de connexion internet")
            }
        }
    }

    companion object {
        private const val TAG = "NewsViewModel"
    }
}