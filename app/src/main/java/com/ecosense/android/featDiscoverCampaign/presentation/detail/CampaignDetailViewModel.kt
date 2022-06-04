package com.ecosense.android.featDiscoverCampaign.presentation.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featDiscoverCampaign.domain.repository.DiscoverCampaignRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CampaignDetailViewModel @Inject constructor(
    private val discoverCampaignRepository: DiscoverCampaignRepository
) : ViewModel() {
    private val _state = mutableStateOf(CampaignDetailScreenState.defaultValue)
    val state: State<CampaignDetailScreenState> = _state

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private var setCampaignIdJob: Job? = null
    fun setCampaignId(id: Int) {
        setCampaignIdJob?.cancel()
        setCampaignIdJob = viewModelScope.launch {
            discoverCampaignRepository.getCampaignDetail(id).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingCampaignDetail = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            campaignDetail = result.data ?: state.value.campaignDetail,
                            isLoadingCampaignDetail = true
                        )
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            campaignDetail = result.data ?: state.value.campaignDetail,
                            isLoadingCampaignDetail = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}