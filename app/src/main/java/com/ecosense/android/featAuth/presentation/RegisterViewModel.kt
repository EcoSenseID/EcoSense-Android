package com.ecosense.android.featAuth.presentation

import androidx.lifecycle.ViewModel
import com.ecosense.android.core.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    repository: AuthRepository
) : ViewModel() {
    // TODO: not yet implemented
}