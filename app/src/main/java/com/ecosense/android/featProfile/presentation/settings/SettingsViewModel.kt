package com.ecosense.android.featProfile.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.domain.model.User
import com.ecosense.android.core.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    var userState by mutableStateOf(User.defaultValue)
        private set

    init {
        onRefreshUserState()
    }

    private var onRefreshUserStateJob: Job? = null
    fun onRefreshUserState() {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.let { userState = it }
        }
    }

    private var onLogoutClickJob: Job? = null
    fun onLogoutClick() {
        onLogoutClickJob?.cancel()
        onLogoutClickJob = viewModelScope.launch {
            authRepository.logout()
        }
    }
}