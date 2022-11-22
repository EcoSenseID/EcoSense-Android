package com.ecosense.android.featDiscoverCampaign.presentation.detail

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.R
import com.ecosense.android.core.domain.constants.CampaignCompletionStatus
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featDiscoverCampaign.domain.repository.DiscoverCampaignRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CampaignDetailViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val discoverCampaignRepository: DiscoverCampaignRepository
) : ViewModel() {

    private var campaignRecordId by mutableStateOf<Int?>(null)

    val isLoggedIn: StateFlow<Boolean?> = authRepository.isLoggedIn
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _state = mutableStateOf(CampaignDetailScreenState.defaultValue)
    val state: State<CampaignDetailScreenState> = _state

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private var setCampaignIdJob: Job? = null
    fun setCampaignId(id: Int, recordId: Int?) {
        setCampaignIdJob?.cancel()
        _state.value = state.value.copy(proofPhotoUrl = null)
        setCampaignIdJob = viewModelScope.launch {
            campaignRecordId = recordId
            discoverCampaignRepository.getCampaignDetail(
                id = id,
                recordId = recordId,
            ).onEach { result ->
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
                        checkAllMissionIsReadyToSend()
                    }
                }
            }.launchIn(this)
        }
    }

    private var onUploadCompletionProofJob: Job? = null
    fun onUploadCompletionProof(caption: String?, missionId: Int, campaignId: Int) {
        onUploadCompletionProofJob?.cancel()
        onUploadCompletionProofJob = viewModelScope.launch {
            discoverCampaignRepository.setCompletionProof(
                photo = state.value.proofPhotoUrl,
                caption = caption,
                missionId = missionId
            ).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = state.value.copy(isLoadingUploadProof = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoadingUploadProof = true)
                        _eventFlow.send(UIEvent.HideKeyboard)
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(isLoadingUploadProof = false)
                        _eventFlow.send(UIEvent.ShowSnackbar(UIText.StringResource(R.string.upload_proof_success)))
                        setCampaignId(id = campaignId, recordId = campaignRecordId)
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
                        _eventFlow.send(UIEvent.ShowSnackbar(UIText.StringResource(R.string.join_campaign_success)))
                        setCampaignId(id = campaignId, recordId = campaignRecordId)
                    }
                }
            }.launchIn(this)
        }
    }

    private var onCompleteCampaignJob: Job? = null
    fun onCompleteCampaign(campaignId: Int) {
        onCompleteCampaignJob?.cancel()
        onCompleteCampaignJob = viewModelScope.launch {
            discoverCampaignRepository.setCompleteCampaign(campaignId = campaignId)
                .onEach { result ->
                    when (result) {
                        is Resource.Error -> {
                            _state.value = state.value.copy(isLoadingCompleteCampaign = false)
                            result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(isLoadingCompleteCampaign = true)
                        }
                        is Resource.Success -> {
                            _state.value = state.value.copy(isLoadingCompleteCampaign = false)
                            _eventFlow.send(UIEvent.ShowSnackbar(UIText.StringResource(R.string.submit_campaign_success)))
                            setCampaignId(id = campaignId, recordId = campaignRecordId)
                        }
                    }
                }.launchIn(this)
        }
    }

    suspend fun getNewTempJpegUri(): Uri {
        val uri = discoverCampaignRepository.getNewTempJpegUri()
        _state.value = state.value.copy(tempJpegUri = uri)
        return uri
    }

    fun onImagePicked(uri: Uri?) {
        uri?.let { _state.value = state.value.copy(proofPhotoUrl = it.toString()) }
    }

    fun onImageCaptured() {
        _state.value = state.value.copy(
            proofPhotoUrl = state.value.tempJpegUri.toString()
        )
    }

    private fun checkAllMissionIsReadyToSend() {
        val countCompletedMission = state.value.campaignDetail.missions.filter {
            it.completionStatus == CampaignCompletionStatus.COMPLETED
        }.size
        val countVerifiedMission = state.value.campaignDetail.missions.filter {
            it.completionStatus == CampaignCompletionStatus.IN_VERIFICATION
        }.size
        val countReadyToSendMission = countCompletedMission + countVerifiedMission

        if (countReadyToSendMission == state.value.campaignDetail.missions.size) {
            _state.value = state.value.copy(allMissionIsReadyToSend = true)
        } else {
            _state.value = state.value.copy(allMissionIsReadyToSend = false)
        }
    }
}