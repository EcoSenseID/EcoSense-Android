package com.ecosense.android.featProfile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.domain.model.User
import com.ecosense.android.core.domain.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {
    val user: StateFlow<User> = authenticationRepository.getUser()
        .stateIn(viewModelScope, SharingStarted.Lazily, User.defaultValue)

    private var onLogoutClickJob: Job? = null
    fun onLogoutClick() {
        onLogoutClickJob?.cancel()
        onLogoutClickJob = viewModelScope.launch {
            authenticationRepository.logout()
        }
    }
}