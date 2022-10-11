package com.ecosense.android.featReward.presentation.list.myrewards

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
class MyRewardsViewModel @Inject constructor(
    private val rewardRepository: RewardRepository
) : ViewModel() {
    private val _state = mutableStateOf(MyRewardsScreenState.defaultValue)
    val state: State<MyRewardsScreenState> = _state

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private var getMyRewardsJob: Job? = null
    fun getMyRewards() {
        getMyRewardsJob?.cancel()
        getMyRewardsJob = viewModelScope.launch {
            rewardRepository.getMyRewards().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingMyRewardList = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            myRewards = result.data ?: state.value.myRewards,
                            isLoadingMyRewardList = true
                        )
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            myRewards = result.data ?: state.value.myRewards,
                            isLoadingMyRewardList = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    private var onUseRewardJob: Job? = null
    fun onUseRewardJob(rewardId: Int) {
        onUseRewardJob?.cancel()
        onUseRewardJob = viewModelScope.launch {
            rewardRepository.setUseReward(rewardId = rewardId).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingUseReward = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoadingUseReward = true)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isLoadingUseReward = false)
                        _eventFlow.send(UIEvent.ShowSnackbar(UIText.StringResource(R.string.join_campaign_success)))
                        getMyRewards()
                    }
                }
            }.launchIn(this)
        }
    }
}