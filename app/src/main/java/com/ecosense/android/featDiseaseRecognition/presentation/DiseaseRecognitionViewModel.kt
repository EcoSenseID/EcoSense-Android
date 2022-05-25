package com.ecosense.android.featDiseaseRecognition.presentation

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ecosense.android.featDiseaseRecognition.domain.model.RecognisedDisease
import com.ecosense.android.featDiseaseRecognition.domain.repository.DiseaseRecognitionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiseaseRecognitionViewModel @Inject constructor(
    private val diseaseRecognitionRepository: DiseaseRecognitionRepository
) : ViewModel() {
    private val _state = mutableStateOf(DiseaseRecognitionState.defaultValue)
    val state: State<DiseaseRecognitionState> = _state

    fun onAnalyze(bitmap: Bitmap) {
        _state.value = state.value.copy(
            recognisedDiseasesList = diseaseRecognitionRepository.analyzeDiseases(bitmap)
        )
    }
}