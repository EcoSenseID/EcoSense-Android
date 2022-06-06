package com.ecosense.android.featDiscoverCampaign.presentation.detail

import android.net.Uri
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
            discoverCampaignRepository.getCampaignDetail(id = id).onEach { result ->
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

    private var onUploadCompletionProofJob: Job? = null
    fun onUploadCompletionProof(caption: String?, taskId: Int) {
        onUploadCompletionProofJob?.cancel()
        onUploadCompletionProofJob = viewModelScope.launch {
            discoverCampaignRepository.setCompletionProof(
                photo = state.value.proofPhotoUrl,
                caption = caption,
                taskId = taskId
            ).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingUploadProof = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoadingUploadProof = true)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isLoadingUploadProof = false)
                    }
                }
            }.launchIn(this)
        }
    }

    private var onJoinCampaignJob: Job? = null
    fun onJoinCampaign(campaignId: Int) {
        onJoinCampaignJob?.cancel()
        onJoinCampaignJob = viewModelScope.launch {
            discoverCampaignRepository.setJoinCampaign(campaignId = campaignId).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingJoinCampaign = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoadingJoinCampaign = true)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isLoadingJoinCampaign = false)
                    }
                }
            }.launchIn(this)
        }
    }

    fun onImagePicked(uri: Uri?) {
        uri?.let { _state.value = state.value.copy(proofPhotoUrl = it.toString()) }
    }
}