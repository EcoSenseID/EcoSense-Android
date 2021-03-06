package com.ecosense.android.featRecognition.presentation.recognition

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.R
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featRecognition.domain.model.Recognisable
import com.ecosense.android.featRecognition.domain.repository.RecognitionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
            .recognise(bitmap)
            .sortedByDescending { it.confidencePercent }

        val mainDiagnosis: Recognisable? = try {
            diagnoses.first { it.confidencePercent > MAIN_DIAGNOSIS_MINIMUM_CONFIDENCE_PERCENT }
        } catch (e: NoSuchElementException) {
            null
        }

        val diffDiagnoses: List<Recognisable>? = when {
            mainDiagnosis != null -> diagnoses
                .filterNot { it.readableName == mainDiagnosis.readableName }
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
            val recognisable = state.value.mainDiagnosis ?: return@launch
            recognitionRepository.saveRecognisable(
                label = recognisable.label,
                confidencePercent = recognisable.confidencePercent,
            ).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isSavingResult = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isSavingResult = true)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isSavingResult = false)
                        _eventFlow.send(UIEvent.ShowSnackbar(UIText.StringResource(R.string.sm_saved_successfully)))
                    }
                }
            }.launchIn(this)
        }
    }

    companion object {
        const val MAIN_DIAGNOSIS_MINIMUM_CONFIDENCE_PERCENT = 40
        const val DIFFERENTIAL_DIAGNOSES_MINIMUM_CONFIDENCE_PERCENT = 30
    }
}