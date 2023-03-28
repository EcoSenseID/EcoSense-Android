package com.ecosense.android.featForums.presentation.storyComposer

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.core.presentation.model.SharedCampaignPresentation
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featForums.domain.repository.ForumsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryComposerViewModel @Inject constructor(
    private val forumsRepository: ForumsRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    var state by mutableStateOf(StoryComposerState.defaultValue)
        private set

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.let {
                state = state.copy(
                    avatarUrl = it.photoUrl,
                )
            }
        }
    }

    private var onChangeCaptionJob: Job? = null
    fun onChangeCaption(value: String) {
        onChangeCaptionJob?.cancel()
        onChangeCaptionJob = viewModelScope.launch {
            state = state.copy(
                caption = value,
            )
        }
    }

    private var onClickSendJob: Job? = null
    fun onClickSend() {
        onClickSendJob?.cancel()
        onClickSendJob = viewModelScope.launch {
            _eventFlow.send(UIEvent.HideKeyboard)

            forumsRepository.postNewStory(
                caption = state.caption,
                attachedPhoto = state.attachedPhotoUri?.let { forumsRepository.findJpegByUri(it) },
                sharedCampaignId = state.sharedCampaign?.id,
            ).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        state = state.copy(isUploading = false)
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }

                    is Resource.Loading -> {
                        state = state.copy(isUploading = true)
                    }

                    is Resource.Success -> {
                        state = state.copy(isUploading = false)
                        _eventFlow.send(UIEvent.Finish)
                    }
                }
            }.launchIn(this)
        }
    }

    fun onImagePicked(uri: Uri?) {
        uri?.let { state = state.copy(attachedPhotoUri = it) }
    }

    private var onReceivedSharedCampaignJob: Job? = null
    fun onReceivedSharedCampaign(campaign: SharedCampaignPresentation) {
        onReceivedSharedCampaignJob?.cancel()
        onReceivedSharedCampaignJob = viewModelScope.launch {
            state = state.copy(sharedCampaign = campaign)
        }
    }
}