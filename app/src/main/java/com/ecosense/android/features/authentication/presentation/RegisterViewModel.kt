package com.ecosense.android.features.authentication.presentation

import androidx.lifecycle.ViewModel
import com.ecosense.android.features.authentication.domain.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    repository: AuthenticationRepository
) : ViewModel() {
    // TODO: not yet implemented
}