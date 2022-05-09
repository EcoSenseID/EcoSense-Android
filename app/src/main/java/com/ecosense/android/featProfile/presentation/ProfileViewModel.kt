package com.ecosense.android.featProfile.presentation

import androidx.lifecycle.ViewModel
import com.ecosense.android.featProfile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {
    // TODO: not yet implemented
}