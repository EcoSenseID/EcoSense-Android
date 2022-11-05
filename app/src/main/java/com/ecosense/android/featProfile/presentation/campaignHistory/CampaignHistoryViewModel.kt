package com.ecosense.android.featProfile.presentation.campaignHistory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featProfile.domain.repository.ProfileRepository
import com.ecosense.android.featProfile.presentation.profile.model.RecentCampaignPresentation
import com.ecosense.android.featProfile.presentation.profile.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CampaignHistoryViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    var campaigns by mutableStateOf(emptyList<RecentCampaignPresentation>())
        private set

    var isLoading by mutableStateOf(false)
        private set

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    init {
        viewModelScope.launch {
            profileRepository.getCampaignsHistory().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        isLoading = false
                        result.uiText?.let { _eventFlow.send(UIEvent.ShowSnackbar(it)) }
                    }
                    is Resource.Loading -> isLoading = true
                    is Resource.Success -> {
                        isLoading = false
                        result.data?.let { campaignsList ->
                            campaigns = campaignsList.map { it.toPresentation() }
                        }
                    }
                }
            }.launchIn(this)
        }
    }
}