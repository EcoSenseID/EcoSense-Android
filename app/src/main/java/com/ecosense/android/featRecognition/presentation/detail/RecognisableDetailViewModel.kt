package com.ecosense.android.featRecognition.presentation.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ecosense.android.featRecognition.domain.repository.RecognitionRepository
import com.ecosense.android.featRecognition.presentation.model.RecognisableDetailParcelable
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecognisableDetailViewModel @Inject constructor(
    private val recognitionRepository: RecognitionRepository
) : ViewModel() {

    private val _state = mutableStateOf(RecognisableDetailScreenState.defaultValue)
    val state: State<RecognisableDetailScreenState> = _state

    fun setDetailParcelable(recognisableDetailParcelable: RecognisableDetailParcelable) {
        val disease = recognitionRepository.getDisease(recognisableDetailParcelable.label)
        _state.value = state.value.copy(
            recognisableDetail = recognisableDetailParcelable.toRecognisableDetail(),
            disease = disease ?: state.value.disease,
        )
    }
}