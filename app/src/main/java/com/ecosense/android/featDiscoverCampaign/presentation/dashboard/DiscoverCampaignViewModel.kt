package com.ecosense.android.featDiscoverCampaign.presentation.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featDiscoverCampaign.domain.model.Dashboard
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
class DiscoverCampaignViewModel @Inject constructor(
    private val discoverCampaignRepository: DiscoverCampaignRepository
) : ViewModel() {

    private val _state = mutableStateOf(DiscoverCampaignScreenState.defaultValue)
    val state: State<DiscoverCampaignScreenState> = _state

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        getDashboard()
        getCategories()
    }

    private var getDashboardJob: Job? = null
    private fun getDashboard() {
        getDashboardJob?.cancel()
        getDashboardJob = viewModelScope.launch {
            discoverCampaignRepository.getDashboard().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingDashboard = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            dashboard = result.data ?: Dashboard.defaultValue,
                            isLoadingDashboard = true
                        )
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            dashboard = result.data ?: Dashboard.defaultValue,
                            isLoadingDashboard = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    private var getCategoriesJob: Job? = null
    private fun getCategories() {
        getCategoriesJob?.cancel()
        getCategoriesJob = viewModelScope.launch {
            discoverCampaignRepository.getCategories().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingCategories = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            categories = result.data ?: emptyList(),
                            isLoadingCategories = true
                        )
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            categories = result.data ?: emptyList(),
                            isLoadingCategories = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }
}