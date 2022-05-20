package com.ecosense.android.featAuth.presentation.resetPassword

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = mutableStateOf(ResetPasswordScreenState.defaultValue)
    val state: State<ResetPasswordScreenState> = _state

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    fun onEmailValueChange(value: String) {
        _state.value = state.value.copy(email = value)
    }

    private var onSendInstructionClickJob: Job? = null
    fun onSendInstructionClick() {
        onSendInstructionClickJob?.cancel()
        onSendInstructionClickJob = viewModelScope.launch {
            repository.sendPasswordResetEmail(email = state.value.email).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            isLoading = false,
                            isSuccessful = false,
                        )
                        result.uiText?.let { _eventFlow.trySend(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            isLoading = true,
                            isSuccessful = false,
                        )
                        _eventFlow.trySend(UIEvent.HideKeyboard)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            isLoading = false,
                            isSuccessful = true
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}