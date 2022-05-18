package com.ecosense.android.featAuth.presentation

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
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _isPasswordVisible = mutableStateOf(false)
    val isPasswordVisible: State<Boolean> = _isPasswordVisible

    fun onEmailValueChange(value: String) {
        _email.value = value
    }

    fun onPasswordValueChange(value: String) {
        _password.value = value
    }

    fun onChangePasswordVisibility() {
        _isPasswordVisible.value = !isPasswordVisible.value
    }

    private var onLoginClickJob: Job? = null
    fun onLoginClick() {
        onLoginClickJob?.cancel()
        onLoginClickJob = viewModelScope.launch {
            repository.login(email = email.value, password = password.value).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        Log.d("TAG", "onLoginClick: ERROR")
                    }
                    is Resource.Loading -> {
                        Log.d("TAG", "onLoginClick: LOADING")
                    }
                    is Resource.Success -> {
                        Log.d("TAG", "onLoginClick: SUCCESS")
                    }
                }
            }.launchIn(this)
        }
    }

    fun onLoginWithGoogleClick() {
        Log.d("TAG", "onLoginWithGoogleClick: ")
    }
}