package com.ecosense.android.featRecognition.presentation.recognition

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.featRecognition.domain.model.RecognitionResult
import com.ecosense.android.featRecognition.domain.repository.RecognitionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecognitionViewModel @Inject constructor(
    private val recognitionRepository: RecognitionRepository
) : ViewModel() {

    private val _state = mutableStateOf(RecognitionState.defaultValue)
    val state: State<RecognitionState> = _state

    fun onAnalyze(bitmap: Bitmap) {
        val diagnoses = recognitionRepository
            .analyzeDiseases(bitmap)
            .sortedByDescending { it.confidence }

        val mainDiagnosis: RecognitionResult? = try {
            diagnoses.first { it.confidence > MAIN_DIAGNOSIS_MINIMUM_CONFIDENCE }
        } catch (e: NoSuchElementException) {
            null
        }

        val diffDiagnoses: List<RecognitionResult>? = when {
            mainDiagnosis != null -> diagnoses.filter {
                it != mainDiagnosis && it.confidence > DIFFERENTIAL_DIAGNOSES_MINIMUM_CONFIDENCE
            }

            else -> null
        }

        _state.value = state.value.copy(
            mainDiagnosis = mainDiagnosis,
            diffDiagnoses = diffDiagnoses,
        )
    }

    fun onSaveResult() {
        viewModelScope.launch {
            state.value.mainDiagnosis?.let {
                recognitionRepository.saveRecognitionResult(it)
            }
        }
    }

    companion object {
        private const val MAIN_DIAGNOSIS_MINIMUM_CONFIDENCE = 0.40f
        private const val DIFFERENTIAL_DIAGNOSES_MINIMUM_CONFIDENCE = 0.30f
    }
}