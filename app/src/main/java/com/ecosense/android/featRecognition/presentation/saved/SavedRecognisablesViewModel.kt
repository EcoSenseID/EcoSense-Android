package com.ecosense.android.featRecognition.presentation.saved

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featRecognition.domain.repository.RecognitionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedRecognisablesViewModel @Inject constructor(
    private val recognitionRepository: RecognitionRepository
) : ViewModel() {

    private val _state = mutableStateOf(SavedRecognisablesScreenState.defaultValue)
    val state: State<SavedRecognisablesScreenState> = _state

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        getHistoryList()
    }

    private var getHistoryListJob: Job? = null
    private fun getHistoryList() {
        getHistoryListJob?.cancel()
        getHistoryListJob = viewModelScope.launch {
            recognitionRepository.getSavedRecognisables().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoading = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoading = true)
                        _eventFlow.send(UIEvent.HideKeyboard)
                    }
                    is Resource.Success -> {
                        // TODO: create sorting feature
                        _state.value = state.value.copy(
                            isLoading = false,
                            savedRecognisables = result.data?.sortedByDescending { it.savedAt }
                                ?: emptyList()
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}