package com.ecosense.android.featRecognition.presentation.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featRecognition.domain.repository.RecognitionRepository
import com.ecosense.android.featRecognition.presentation.model.RecognisableDetailParcelable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecognisableDetailViewModel @Inject constructor(
    private val recognitionRepository: RecognitionRepository
) : ViewModel() {

    private val _state = mutableStateOf(RecognisableDetailScreenState.defaultValue)
    val state: State<RecognisableDetailScreenState> = _state

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    fun setDetailParcelable(recognisableDetailParcelable: RecognisableDetailParcelable) {
        val disease = recognitionRepository.getDisease(recognisableDetailParcelable.label)
        _state.value = state.value.copy(
            recognisableDetail = recognisableDetailParcelable.toRecognisableDetail(),
            disease = disease ?: state.value.disease,
        )
    }

    private var onSaveClick: Job? = null
    fun onSaveClick() {
        onSaveClick?.cancel()
        onSaveClick = viewModelScope.launch {
            recognitionRepository
                .saveRecognisable(
                    label = state.value.recognisableDetail.label,
                    confidencePercent = state.value.recognisableDetail.confidencePercent,
                )
                .onEach { result ->
                    when (result) {
                        is Resource.Error -> {
                            _state.value = state.value.copy(isLoadingSaving = false)
                            result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(isLoadingSaving = true)
                        }
                        is Resource.Success -> {
                            result.data?.let {
                                _state.value = state.value.copy(
                                    isLoadingSaving = false,
                                    recognisableDetail = state.value.recognisableDetail.copy(id = it)
                                )
                            }
                        }
                    }
                }.launchIn(this)
        }
    }
}