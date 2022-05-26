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
        val diagnoses = diseaseRecognitionRepository
            .analyzeDiseases(bitmap)
            .sortedByDescending { it.confidence }

        val mainDiagnosis: RecognisedDisease? = try {
            diagnoses.first { it.confidence > MAIN_DIAGNOSIS_MINIMUM_CONFIDENCE }
        } catch (e: NoSuchElementException) {
            null
        }

        val diffDiagnoses: List<RecognisedDisease>? = when {
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

    companion object {
        private const val MAIN_DIAGNOSIS_MINIMUM_CONFIDENCE = 0.70f
        private const val DIFFERENTIAL_DIAGNOSES_MINIMUM_CONFIDENCE = 0.50f
    }
}