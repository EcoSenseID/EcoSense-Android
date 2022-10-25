package com.ecosense.android.featProfile.presentation.changeEmail

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
class ChangeEmailViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    var currentEmailValue by mutableStateOf("")
        private set

    var newEmailValue by mutableStateOf("")
        private set

    var passwordValue by mutableStateOf("")
        private set

    var passwordVisibility by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.email?.let { currentEmailValue = it }
        }
    }

    private var onValueChangeNewEmailJob: Job? = null
    fun onValueChangeNewEmail(value: String) {
        onValueChangeNewEmailJob?.cancel()
        onValueChangeNewEmailJob = viewModelScope.launch {
            newEmailValue = value
        }
    }

    private var onValueChangePasswordJob: Job? = null
    fun onValueChangePassword(value: String) {
        onValueChangePasswordJob?.cancel()
        onValueChangePasswordJob = viewModelScope.launch {
            passwordValue = value
        }
    }

    private var onToggleVisibilityPasswordJob: Job? = null
    fun onToggleVisibilityPassword() {
        onToggleVisibilityPasswordJob?.cancel()
        onToggleVisibilityPasswordJob = viewModelScope.launch {
            passwordVisibility = !passwordVisibility
        }
    }

    private var onClickSubmitJob: Job? = null
    fun onClickSubmit() {
        onClickSubmitJob?.cancel()
        onClickSubmitJob = viewModelScope.launch {
            authRepository.updateEmail(
                password = passwordValue,
                newEmail = newEmailValue,
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