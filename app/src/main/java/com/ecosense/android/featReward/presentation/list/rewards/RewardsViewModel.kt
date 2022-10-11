package com.ecosense.android.featReward.presentation.list.rewards

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.R
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featReward.domain.repository.RewardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardsViewModel @Inject constructor(
    private val rewardRepository: RewardRepository
) : ViewModel() {
    private val _state = mutableStateOf(RewardsScreenState.defaultValue)
    val state: State<RewardsScreenState> = _state

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private var getRewardsJob: Job? = null
    fun getRewards(rewardCategory: String) {
        getRewardsJob?.cancel()
        getRewardsJob = viewModelScope.launch {
            rewardRepository.getRewards(rewardCategory = rewardCategory).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingRewardList = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            rewards = result.data ?: state.value.rewards,
                            isLoadingRewardList = true
                        )
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            rewards = result.data ?: state.value.rewards,
                            isLoadingRewardList = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    private var onRedeemRewardJob: Job? = null
    fun onRedeemRewardJob(rewardId: Int) {
        onRedeemRewardJob?.cancel()
        onRedeemRewardJob = viewModelScope.launch {
            rewardRepository.setRedeemReward(rewardId = rewardId).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingRedeemReward = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoadingRedeemReward = true)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isLoadingRedeemReward = false)
                        _eventFlow.send(UIEvent.ShowSnackbar(UIText.StringResource(R.string.join_campaign_success)))
                    }
                }
            }.launchIn(this)
        }
    }
}