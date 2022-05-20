package com.ecosense.android.featProfile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.domain.model.User
import com.ecosense.android.core.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    val user: StateFlow<User> = authRepository.getUser()
        .stateIn(viewModelScope, SharingStarted.Lazily, User.defaultValue)

    private var onLogoutClickJob: Job? = null
    fun onLogoutClick() {
        onLogoutClickJob?.cancel()
        onLogoutClickJob = viewModelScope.launch {
            authRepository.logout()
        }
    }
}