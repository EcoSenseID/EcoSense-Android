package com.ecosense.android.featProfile.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featProfile.domain.model.Contributions
import com.ecosense.android.featProfile.domain.repository.ProfileRepository
import com.ecosense.android.featProfile.presentation.profile.model.ProfileScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _state = mutableStateOf(ProfileScreenState.defaultValue)
    val state: State<ProfileScreenState> = _state

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        getProfile()
        getContributions()
    }

    private var getProfileJob: Job? = null
    private fun getProfile() {
        getProfileJob?.cancel()
        getProfileJob = viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            user?.let { _state.value = state.value.copy(user = it) }
        }
    }

    private var getContributionsJob: Job? = null
    private fun getContributions() {
        getContributionsJob?.cancel()
        getContributionsJob = viewModelScope.launch {
            profileRepository.getContributions().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingContributions = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            contributions = result.data ?: Contributions.defaultValue,
                            isLoadingContributions = true
                        )
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            contributions = result.data ?: Contributions.defaultValue,
                            isLoadingContributions = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    private var onLogoutClickJob: Job? = null
    fun onLogoutClick() {
        onLogoutClickJob?.cancel()
        onLogoutClickJob = viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun setExpandDropdownMenu(visible: Boolean) {
        _state.value = state.value.copy(
            isDropdownMenuExpanded = visible
        )
    }
}