package com.example.mealmate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.example.mealmate.ui.view.communityScreen.aiOption.chat.ChatViewModel
import com.example.mealmate.ui.view.communityScreen.aiOption.multiModal.PhotoReasoningViewModel
import com.example.mealmate.ui.view.communityScreen.aiOption.text.SummarizeViewModel


val GenerativeViewModelFactory = object : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(
        viewModelClass: Class<T>,
        extras: CreationExtras
    ): T{
        // Définir la configuration de la génération de texte pour le modèle Gemini.
        val config = generationConfig {
            temperature = 0.5f // La température contrôle l'aléatoire des réponses.
        }

        // Utiliser la fonction `with` pour simplifier l'accès aux méthodes de `viewModelClass`.
        return  with(viewModelClass) {
            when{
                // pour la génération de texte.
                isAssignableFrom(SummarizeViewModel::class.java) -> {
                    // Initialiser un GenerativeModel avec le modèle d'IA `gemini-flash`.
                    val generativeModel = GenerativeModel(
                        modelName = "gemini-1.5-flash-latest", // Nom du modèle Gemini à utiliser.
                        generationConfig = config, // Configuration de la génération (température, etc.).
                        apiKey = BuildConfig.apiKey // Clé API pour accéder au modèle (depuis `BuildConfig`).
                    )
                    // Créer une instance de `SummarizeViewModel`, en lui passant `generativeModel`.
                    SummarizeViewModel(generativeModel)
                }

                // pour la génération de texte multimodal.
                isAssignableFrom(PhotoReasoningViewModel::class.java) ->{
                    // Initialiser un GenerativeModel avec le modèle d'IA `gemini-flash`.
                    val generativeModel = GenerativeModel(
                        modelName = "gemini-1.5-flash-latest", // Nom du modèle Gemini à utiliser.
                        generationConfig = config, // Configuration de la génération (température, etc.).
                        apiKey = BuildConfig.apiKey // Clé API pour accéder au modèle (depuis `BuildConfig`).
                    )
                    PhotoReasoningViewModel(generativeModel)
                }

                //pour le chat
                isAssignableFrom(ChatViewModel::class.java) ->{
                    // Initialiser un GenerativeModel avec le modèle d'IA `gemini-flash`.
                    val generativeModel = GenerativeModel(
                        modelName = "gemini-1.5-flash-latest", // Nom du modèle Gemini à utiliser.
                        generationConfig = config, // Configuration de la génération (température, etc.).
                        apiKey = BuildConfig.apiKey // Clé API pour accéder au modèle (depuis `BuildConfig`).
                    )
                    ChatViewModel(generativeModel)
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${viewModelClass.name}")
            }
        }as T
    }
}