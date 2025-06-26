package com.example.mealmate.data.source.model

import java.util.Date

// Informations médicales de base avec champs obligatoires/optionnels
data class MedicalParameters(
    val userId: String = "",
    val birthDate: String? = null, // Format: "YYYY-MM-DD" (OBLIGATOIRE)
    val height: Double? = null, // en cm
    val initialWeight: Double? = null, // en kg (poids avant grossesse)
    val currentWeight: Double? = null, // en kg
    val bloodType: String? = null, // "A+", "B-", etc.
    val rhFactor: String? = null, // "+" ou "-"
    val previousPregnancies: Int = 0,
    val previousComplications: List<String> = emptyList(),
    val allergies: List<String> = emptyList(),
    val chronicDiseases: List<String> = emptyList(),
    val currentMedications: List<String> = emptyList()
){
    // Fonction utilitaire pour créer une copie avec les nouvelles valeurs
    fun updateFromMap(updates: Map<String, Any?>): MedicalParameters {
        return this.copy(
            height = updates["height"] as? Double ?: height,
            initialWeight = updates["initialWeight"] as? Double ?: initialWeight,
            currentWeight = updates["currentWeight"] as? Double ?: currentWeight,
            bloodType = updates["bloodType"] as? String ?: bloodType,
            birthDate = (updates["birthDate"] as? String ?: birthDate)
        )
    }
}