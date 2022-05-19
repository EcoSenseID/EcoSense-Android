package com.ecosense.android.featAuth.presentation.register

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _isPasswordVisible = mutableStateOf(false)
    val isPasswordVisible: State<Boolean> = _isPasswordVisible

    private val _passwordVerif = mutableStateOf("")
    val passwordVerif: State<String> = _passwordVerif

    private val _isPasswordVerifVisible = mutableStateOf(false)
    val isPasswordVerifVisible: State<Boolean> = _isPasswordVerifVisible

    fun onEmailValueChange(value: String) {
        _email.value = value
    }

    fun onPasswordValueChange(value: String) {
        _password.value = value
    }

    fun onPasswordVerifValueChange(value: String) {
        _passwordVerif.value = value
    }

    fun onChangePasswordVisibility() {
        _isPasswordVisible.value = !isPasswordVisible.value
    }

    fun onChangePasswordVerifVisibility() {
        _isPasswordVerifVisible.value = !isPasswordVerifVisible.value
    }

    private var onRegisterClickJob: Job? = null
    fun onRegisterClick() {
        onRegisterClickJob?.cancel()
        onRegisterClickJob = viewModelScope.launch {
            authRepository.registerWithEmail(
                email = email.value,
                password = password.value,
                passwordVerif = passwordVerif.value
            ).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        logcat { "onRegisterClick: ERROR" }
                    }
                    is Resource.Loading -> {
                        logcat { "onRegisterClick: LOADING" }
                    }
                    is Resource.Success -> {
                        logcat { "onRegisterClick: SUCCESS" }
                    }
                }
            }.launchIn(this)
        }
    }
}