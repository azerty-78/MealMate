package com.example.mealmate.ui.view.profileScreen

import com.example.mealmate.data.source.model.MedicalParameters

data class MedicalParamState(
    val currentParameters: MedicalParameters? = null,
    val isLoading: Boolean = false,
    val isError: String? = null,
    val isSuccess: Boolean = false,
)
