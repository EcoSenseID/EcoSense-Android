package com.ecosense.android.featDiscoverCampaign.presentation.browse

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.featDiscoverCampaign.domain.repository.DiscoverCampaignRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class BrowseCampaignViewModel @Inject constructor(
    private val discoverCampaignRepository: DiscoverCampaignRepository
) : ViewModel() {

    private val _state = mutableStateOf(BrowseCampaignScreenState.defaultValue)
    val state: State<BrowseCampaignScreenState> = _state

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private var getCampaignsJob: Job? = null
    fun setCampaignsParams(q: String?, categoryId: Int?) {
        logcat { q ?: "" }
        logcat { categoryId.toString() }
        getCampaignsJob?.cancel()
        getCampaignsJob = viewModelScope.launch {
            discoverCampaignRepository.getCampaigns(q = q, categoryId = categoryId).onEach { result ->
                logcat { result::class.java.name }
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingCampaigns = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            campaigns = result.data ?: emptyList(),
                            isLoadingCampaigns = true
                        )
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            campaigns = result.data ?: emptyList(),
                            isLoadingCampaigns = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}