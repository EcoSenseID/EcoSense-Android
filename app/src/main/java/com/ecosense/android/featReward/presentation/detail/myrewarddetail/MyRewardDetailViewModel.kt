package com.ecosense.android.featReward.presentation.detail.myrewarddetail

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
class MyRewardDetailViewModel @Inject constructor(
    private val rewardRepository: RewardRepository
) : ViewModel() {
    private val _state = mutableStateOf(MyRewardDetailScreenState.defaultValue)
    val state: State<MyRewardDetailScreenState> = _state

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private var getMyRewardDetail: Job? = null
    fun getMyRewardDetail(claimId: Int) {
        getMyRewardDetail?.cancel()
        getMyRewardDetail = viewModelScope.launch {
            rewardRepository.getMyRewardDetail(claimId = claimId).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingMyRewardDetail = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            myRewardDetail = result.data ?: state.value.myRewardDetail,
                            isLoadingMyRewardDetail = true
                        )
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            myRewardDetail = result.data ?: state.value.myRewardDetail,
                            isLoadingMyRewardDetail = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    private var onUseRewardJob: Job? = null
    fun onUseRewardJob(claimId: Int) {
        onUseRewardJob?.cancel()
        onUseRewardJob = viewModelScope.launch {
            rewardRepository.setUseReward(claimId = claimId).onEach { result ->
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
                        _eventFlow.send(UIEvent.ShowSnackbar(UIText.StringResource(R.string.use_reward_success)))
                        getMyRewardDetail(claimId = claimId)
                    }
                }
            }.launchIn(this)
        }
    }
}