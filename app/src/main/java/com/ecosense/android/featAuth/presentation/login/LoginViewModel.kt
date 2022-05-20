package com.ecosense.android.featAuth.presentation.login

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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = mutableStateOf(LoginScreenState.defaultValue)
    val state: State<LoginScreenState> = _state

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    fun onEmailValueChange(value: String) {
        _state.value = state.value.copy(email = value)
    }

    fun onPasswordValueChange(value: String) {
        _state.value = state.value.copy(password = value)
    }

    fun onTogglePasswordVisibility() {
        _state.value = state.value.copy(isPasswordVisible = !state.value.isPasswordVisible)
    }

    private var onLoginWithEmailClickJob: Job? = null
    fun onLoginWithEmailClick() {
        onLoginWithEmailClickJob?.cancel()
        onLoginWithEmailClickJob = viewModelScope.launch {
            authRepository.loginWithEmail(
                email = state.value.email,
                password = state.value.password
            ).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingEmailLogin = false)
                        result.uiText?.let { _eventFlow.trySend(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoadingEmailLogin = true)
                        _eventFlow.trySend(UIEvent.HideKeyboard)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isLoadingEmailLogin = false)
                    }
                }
            }.launchIn(this)
        }
    }

    private var onGoogleSignInResultJob: Job? = null
    fun onGoogleSignInResult(idToken: String?) {
        onGoogleSignInResultJob?.cancel()
        onGoogleSignInResultJob = viewModelScope.launch {
            authRepository.loginWithGoogle(idToken = idToken).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingGoogleLogin = false)
                        result.uiText?.let { _eventFlow.trySend(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoadingGoogleLogin = true)
                        _eventFlow.trySend(UIEvent.HideKeyboard)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isLoadingGoogleLogin = false)
                    }
                }
            }.launchIn(this)
        }
    }
}