package com.ecosense.android.featReward.presentation.detail.rewarddetail

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
class RewardDetailViewModel @Inject constructor(
    private val rewardRepository: RewardRepository
) : ViewModel() {
    private val _state = mutableStateOf(RewardDetailScreenState.defaultValue)
    val state: State<RewardDetailScreenState> = _state

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    fun onEmailValueChange(value: String) {
        _state.value = state.value.copy(email = value)
    }

    fun onWalletTypeValueChange(value: String) {
        _state.value = state.value.copy(walletType = value)
    }

    fun onWalletNumberValueChange(value: String) {
        _state.value = state.value.copy(walletNumber = value)
    }

    private var getRewardDetail: Job? = null
    fun getRewardDetail(rewardId: Int) {
        getRewardDetail?.cancel()
        getRewardDetail = viewModelScope.launch {
            rewardRepository.getRewardDetail(rewardId = rewardId).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingRewardDetail = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            rewardDetail = result.data ?: state.value.rewardDetail,
                            isLoadingRewardDetail = true
                        )
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            rewardDetail = result.data ?: state.value.rewardDetail,
                            isLoadingRewardDetail = false
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
            rewardRepository.setRedeemReward(rewardId = rewardId)
                .onEach { result ->
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
                            _eventFlow.send(UIEvent.ShowSnackbar(UIText.StringResource(R.string.redeem_reward_success)))
                            getRewardDetail(rewardId = rewardId)
                        }
                    }
                }.launchIn(this)
        }
    }

    private var onRequestRewardJob: Job? = null
    fun onRequestRewardJob(
        rewardId: Int
    ) {
        onRequestRewardJob?.cancel()
        onRequestRewardJob = viewModelScope.launch {
            rewardRepository.setRequestReward(
                rewardId = rewardId,
                email = state.value.email,
                walletType = state.value.walletType,
                walletNumber = state.value.walletNumber
            ).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingRequestReward = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoadingRequestReward = true)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isLoadingRequestReward = false)
                        _eventFlow.send(UIEvent.ShowSnackbar(UIText.StringResource(R.string.request_reward_success)))
                        getRewardDetail(rewardId = rewardId)
                    }
                }
            }.launchIn(this)
        }
    }
}