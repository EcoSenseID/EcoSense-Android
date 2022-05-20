package com.ecosense.android.featAuth.presentation.resetPassword

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
class ResetPasswordViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _email = mutableStateOf("")
    val email: State<String> = _email

    fun onEmailValueChange(value: String) {
        _email.value = value
    }

    private var onSendInstructionClickJob: Job? = null
    fun onSendInstructionClick() {
        onSendInstructionClickJob?.cancel()
        onSendInstructionClickJob = viewModelScope.launch {
            repository.sendPasswordResetEmail(email = email.value).onEach { result ->
                when (result) {
                    // TODO: implement result handling
                    is Resource.Error -> {
                        logcat { "onSendInstructionClick: ERROR" }
                    }
                    is Resource.Loading -> {
                        logcat { "onSendInstructionClick: LOADING" }
                    }
                    is Resource.Success -> {
                        logcat { "onSendInstructionClick: SUCCESS" }
                    }
                }
            }.launchIn(this)
        }
    }
}