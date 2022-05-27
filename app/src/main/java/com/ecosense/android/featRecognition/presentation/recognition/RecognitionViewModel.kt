package com.ecosense.android.featRecognition.presentation.recognition

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.R
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featRecognition.domain.model.RecognitionResult
import com.ecosense.android.featRecognition.domain.repository.RecognitionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecognitionViewModel @Inject constructor(
    private val recognitionRepository: RecognitionRepository
) : ViewModel() {

    private val _state = mutableStateOf(RecognitionState.defaultValue)
    val state: State<RecognitionState> = _state

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    fun onAnalyze(bitmap: Bitmap) {
        val diagnoses = recognitionRepository
            .analyzeDiseases(bitmap)
            .sortedByDescending { it.confidencePercent }

        val mainDiagnosis: RecognitionResult? = try {
            diagnoses.first { it.confidencePercent > MAIN_DIAGNOSIS_MINIMUM_CONFIDENCE_PERCENT }
        } catch (e: NoSuchElementException) {
            null
        }

        val diffDiagnoses: List<RecognitionResult>? = when {
            mainDiagnosis != null -> diagnoses
                .filterNot { it.label == mainDiagnosis.label }
                .filter { it.confidencePercent > DIFFERENTIAL_DIAGNOSES_MINIMUM_CONFIDENCE_PERCENT }

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
                _eventFlow.send(
                    UIEvent.ShowSnackbar(UIText.StringResource(R.string.sm_saved_successfully))
                )
            }
        }
    }

    companion object {
        private const val MAIN_DIAGNOSIS_MINIMUM_CONFIDENCE_PERCENT = 40
        private const val DIFFERENTIAL_DIAGNOSES_MINIMUM_CONFIDENCE_PERCENT = 30
    }
}