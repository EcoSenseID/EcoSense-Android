package com.ecosense.android.features.diseaseRecognition.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ecosense.android.features.diseaseRecognition.domain.RecognisedDisease
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiseaseRecognitionViewModel @Inject constructor() : ViewModel() {
    private val _state = mutableStateOf(DiseaseRecognitionState.defaultValue)
    val state: State<DiseaseRecognitionState> = _state

    fun updateData(recognisedDiseases: List<RecognisedDisease>) {
        _state.value = state.value.copy(
            recognisedDiseasesList = recognisedDiseases
        )
    }
}