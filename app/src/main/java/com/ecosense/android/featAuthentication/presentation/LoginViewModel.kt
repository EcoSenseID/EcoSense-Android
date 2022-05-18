package com.ecosense.android.featAuthentication.presentation

import androidx.lifecycle.ViewModel
import com.ecosense.android.core.domain.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthenticationRepository
) : ViewModel() {
    // TODO: not yet implemented
}