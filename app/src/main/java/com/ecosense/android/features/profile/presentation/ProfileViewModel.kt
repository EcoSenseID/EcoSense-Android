package com.ecosense.android.features.profile.presentation

import androidx.lifecycle.ViewModel
import com.ecosense.android.features.profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {
    // TODO: not yet implemented
}