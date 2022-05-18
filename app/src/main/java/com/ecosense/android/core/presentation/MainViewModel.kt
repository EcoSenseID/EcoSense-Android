package com.ecosense.android.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.domain.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    authenticationRepository: AuthenticationRepository
) : ViewModel() {
    val isLoggedIn: StateFlow<Boolean> = authenticationRepository.isLoggedIn()
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)
}