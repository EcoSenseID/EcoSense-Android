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

//    var q: String? = null
//    var categoryId: Int? = null
//
//    init {
//        setCampaignsParams(q = q, categoryId = categoryId)
//    }

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

//    private var getCampaignsJob: Job? = null
//    fun getCampaigns() {
//        getCampaignsJob?.cancel()
//        getCampaignsJob = viewModelScope.launch {
//            discoverCampaignRepository.getCampaigns().onEach { result ->
//                logcat { result::class.java.name }
//                when (result) {
//                    is Resource.Error -> {
//                        _state.value = state.value.copy(isLoadingCampaigns = false)
//                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
//                    }
//                    is Resource.Loading -> {
//                        _state.value = state.value.copy(
//                            campaigns = result.data ?: emptyList(),
//                            isLoadingCampaigns = true
//                        )
//                    }
//                    is Resource.Success -> {
//                        _state.value = state.value.copy(
//                            campaigns = result.data ?: emptyList(),
//                            isLoadingCampaigns = false
//                        )
//                    }
//                }
//            }.launchIn(this)
//        }
//    }
//    private var getCampaignsByQueryJob: Job? = null
//    fun getCampaignsByQuery(q: String) {
//        logcat { q }
//        getCampaignsByQueryJob?.cancel()
//        getCampaignsByQueryJob = viewModelScope.launch {
//            discoverCampaignRepository.getCampaignsByQuery(q).onEach { result ->
//                logcat { result::class.java.name }
//                when (result) {
//                    is Resource.Error -> {
//                        _state.value = state.value.copy(isLoadingCampaigns = false)
//                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
//                    }
//                    is Resource.Loading -> {
//                        _state.value = state.value.copy(
//                            campaigns = result.data ?: emptyList(),
//                            isLoadingCampaigns = true
//                        )
//                    }
//                    is Resource.Success -> {
//                        _state.value = state.value.copy(
//                            campaigns = result.data ?: emptyList(),
//                            isLoadingCampaigns = false
//                        )
//                    }
//                }
//            }.launchIn(this)
//        }
//    }
//    private var getCampaignsByCategoryJob: Job? = null
//    fun getCampaignsByCategory(categoryId: Int) {
//        logcat { categoryId.toString() }
//        getCampaignsByCategoryJob?.cancel()
//        getCampaignsByCategoryJob = viewModelScope.launch {
//            discoverCampaignRepository.getCampaignsByCategory(categoryId).onEach { result ->
//                logcat { result::class.java.name }
//                when (result) {
//                    is Resource.Error -> {
//                        _state.value = state.value.copy(isLoadingCampaigns = false)
//                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
//                    }
//                    is Resource.Loading -> {
//                        _state.value = state.value.copy(
//                            campaigns = result.data ?: emptyList(),
//                            isLoadingCampaigns = true
//                        )
//                    }
//                    is Resource.Success -> {
//                        _state.value = state.value.copy(
//                            campaigns = result.data ?: emptyList(),
//                            isLoadingCampaigns = false
//                        )
//                    }
//                }
//            }.launchIn(this)
//        }
//    }
}