package com.ecosense.android.featProfile.presentation.changePassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    var currentPasswordValue by mutableStateOf("")
        private set

    var currentPasswordVisibility by mutableStateOf(false)
        private set

    var newPasswordValue by mutableStateOf("")
        private set

    var newPasswordVisibility by mutableStateOf(false)
        private set

    var repeatedNewPasswordValue by mutableStateOf("")
        private set

    var repeatedNewPasswordVisibility by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private var onValueChangeCurrentPasswordJob: Job? = null
    fun onValueChangeCurrentPassword(value: String) {
        onValueChangeCurrentPasswordJob?.cancel()
        onValueChangeCurrentPasswordJob = viewModelScope.launch {
            currentPasswordValue = value
        }
    }

    private var onToggleVisibilityCurrentPasswordJob: Job? = null
    fun onToggleVisibilityCurrentPassword() {
        onToggleVisibilityCurrentPasswordJob?.cancel()
        onToggleVisibilityCurrentPasswordJob = viewModelScope.launch {
            currentPasswordVisibility = !currentPasswordVisibility
        }
    }

    private var onValueChangeNewPasswordJob: Job? = null
    fun onValueChangeNewPassword(value: String) {
        onValueChangeNewPasswordJob?.cancel()
        onValueChangeNewPasswordJob = viewModelScope.launch {
            newPasswordValue = value
        }
    }

    private var onToggleVisibilityNewPasswordJob: Job? = null
    fun onToggleVisibilityNewPassword() {
        onToggleVisibilityNewPasswordJob?.cancel()
        onToggleVisibilityNewPasswordJob = viewModelScope.launch {
            newPasswordVisibility = !newPasswordVisibility
        }
    }

    private var onValueChangeRepeatedNewPasswordJob: Job? = null
    fun onValueChangeRepeatedNewPassword(value: String) {
        onValueChangeRepeatedNewPasswordJob?.cancel()
        onValueChangeRepeatedNewPasswordJob = viewModelScope.launch {
            repeatedNewPasswordValue = value
        }
    }

    private var onToggleVisibilityRepeatedNewPasswordJob: Job? = null
    fun onToggleVisibilityRepeatedNewPassword() {
        onToggleVisibilityRepeatedNewPasswordJob?.cancel()
        onToggleVisibilityRepeatedNewPasswordJob = viewModelScope.launch {
            repeatedNewPasswordVisibility = !repeatedNewPasswordVisibility
        }
    }

    private var onClickSubmitJob: Job? = null
    fun onClickSubmit() {
        onClickSubmitJob?.cancel()
        onClickSubmitJob = viewModelScope.launch {
            authRepository.updatePassword(
                oldPassword = currentPasswordValue,
                newPassword = newPasswordValue,
                repeatedNewPassword = repeatedNewPasswordValue,
            ).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        isLoading = false
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        isLoading = true
                        _eventFlow.send(UIEvent.HideKeyboard)
                    }
                    is Resource.Success -> {
                        isLoading = false
                        val uiText = UIText.StringResource(R.string.sm_changes_saved)
                        _eventFlow.send(UIEvent.ShowSnackbar(uiText))
                        delay(1000)
                        _eventFlow.send(UIEvent.Finish)
                    }
                }
            }.launchIn(this)
        }
    }
}