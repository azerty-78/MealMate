package com.example.mealmate.ui.view.profileScreen

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmate.data.repository.MedicalParametersRepository
import com.example.mealmate.data.repository.UserRepository
import com.example.mealmate.data.source.model.MedicalParameters
import com.example.mealmate.util.ConnectivityObserver
import com.example.mealmate.util.Listener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicalParametersViewModel
@Inject constructor(
    private val medicalParameterRep : MedicalParametersRepository,
    val connectivityObserver: ConnectivityObserver
) : ViewModel() {
    val networkStatus = mutableStateOf(ConnectivityObserver.Status.Neutral)


    private val _uiState = mutableStateOf(MedicalParamState())
    val uiState: State<MedicalParamState> = _uiState


    init {
        getMedicalParameters()
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

    fun createOrUpdateMedicalParameters(parameters: MedicalParameters){
        viewModelScope.launch {
            medicalParameterRep.createOrUpdateMedicalInfo(parameters).collect { result ->
                when(result){
                    Listener.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            isSuccess = false,
                            isError = null
                        )
                    }
                    is Listener.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = true,
                        )
                    }
                    is Listener.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = false,
                            isError = result.message
                        )
                    }
                }
            }
        }
    }

    fun getMedicalParameters(){
        viewModelScope.launch {
            medicalParameterRep.getMedicalInfo().collect { result ->
                when(result){
                    is Listener.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            currentParameters = null,
                            isError = result.message
                        )
                    }
                    is Listener.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true
                        )
                    }
                    is Listener.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            currentParameters = result.data
                        )
                    }
                }
            }
        }
    }
}