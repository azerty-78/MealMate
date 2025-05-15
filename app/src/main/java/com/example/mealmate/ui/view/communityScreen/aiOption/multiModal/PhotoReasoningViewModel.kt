package com.example.mealmate.ui.view.communityScreen.aiOption.multiModal

import androidx.lifecycle.ViewModel
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel


//@HiltViewModel
class PhotoReasoningViewModel(
    private val generativeModel: GenerativeModel
) : ViewModel() {
    // Other ViewModel methods and properties
}