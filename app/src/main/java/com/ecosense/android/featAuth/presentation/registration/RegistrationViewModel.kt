package com.ecosense.android.featAuth.presentation.registration

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.R
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
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

    fun onNameValueChange(value: String) {
        _state.value = state.value.copy(name = value)
    }

    private var onRegisterClickJob: Job? = null
    fun onRegisterClick() {
        onRegisterClickJob?.cancel()
        onRegisterClickJob = viewModelScope.launch {
            if (!state.value.isAgreeTermsPrivacyPolicy) {
                _eventFlow.send(UIEvent.ShowSnackbar(UIText.StringResource(R.string.em_agree_terms_privacy_policy)))
                return@launch
            }

            authRepository.registerWithEmail(
                name = state.value.name,
                email = state.value.email,
                password = state.value.password,
                repeatedPassword = state.value.repeatedPassword
            ).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isRegistering = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isRegistering = true)
                        _eventFlow.send(UIEvent.HideKeyboard)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isRegistering = false)
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
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoadingGoogleLogin = true)
                        _eventFlow.send(UIEvent.HideKeyboard)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isLoadingGoogleLogin = false)
                    }
                }
            }.launchIn(this)
        }
    }

    fun onCheckedChangeTermsPrivacyPolicy() {
        _state.value = state.value.copy(
            isAgreeTermsPrivacyPolicy = !state.value.isAgreeTermsPrivacyPolicy,
        )
    }
}