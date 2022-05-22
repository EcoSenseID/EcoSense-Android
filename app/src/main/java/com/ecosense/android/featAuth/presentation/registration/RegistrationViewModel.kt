package com.ecosense.android.featAuth.presentation.registration

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
class RegistrationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = mutableStateOf(RegistrationScreenState.defaultValue)
    val state: State<RegistrationScreenState> = _state

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    fun onEmailValueChange(value: String) {
        _state.value = state.value.copy(email = value)
    }

    fun onPasswordValueChange(value: String) {
        _state.value = state.value.copy(password = value)
    }

    fun onRepeatedPasswordValueChange(value: String) {
        _state.value = state.value.copy(repeatedPassword = value)
    }

    fun onTogglePasswordVisibility() {
        _state.value = state.value.copy(isPasswordVisible = !state.value.isPasswordVisible)
    }

    fun onToggleRepeatedPasswordVisibility() {
        _state.value = state.value.copy(
            isRepeatedPasswordVisible = !state.value.isRepeatedPasswordVisible
        )
    }

    private var onRegisterClickJob: Job? = null
    fun onRegisterClick() {
        onRegisterClickJob?.cancel()
        onRegisterClickJob = viewModelScope.launch {
            authRepository.registerWithEmail(
                email = state.value.email,
                password = state.value.password,
                repeatedPassword = state.value.repeatedPassword
            ).onEach { result ->
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
                        _state.value = state.value.copy(isLoading = false)
                    }
                }
            }.launchIn(this)
        }
    }
}