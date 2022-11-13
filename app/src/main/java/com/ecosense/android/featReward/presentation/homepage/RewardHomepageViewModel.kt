package com.ecosense.android.featReward.presentation.homepage

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featReward.domain.repository.RewardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardHomepageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val rewardRepository: RewardRepository
) : ViewModel() {
    val isLoggedIn: StateFlow<Boolean?> = authRepository.isLoggedIn
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _state = mutableStateOf(RewardHomepageScreenState.defaultValue)
    val state: State<RewardHomepageScreenState> = _state

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        getProfile()
        getHomepage()
    }

    private var getProfileJob: Job? = null
    private fun getProfile() {
        getProfileJob?.cancel()
        getProfileJob = viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            user?.let { _state.value = state.value.copy(user = it) }
        }
    }

    private var getHomepageJob: Job? = null
    private fun getHomepage() {
        getHomepageJob?.cancel()
        getHomepageJob = viewModelScope.launch {
            rewardRepository.getRewardHomepage().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingRewardHomepage = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            rewardHomepage = result.data ?: state.value.rewardHomepage,
                            isLoadingRewardHomepage = true
                        )
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            rewardHomepage = result.data ?: state.value.rewardHomepage,
                            isLoadingRewardHomepage = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}