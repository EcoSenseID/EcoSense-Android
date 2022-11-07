package com.ecosense.android.featProfile.presentation.campaignHistory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.util.Resource
import com.ecosense.android.featProfile.domain.repository.ProfileRepository
import com.ecosense.android.featProfile.presentation.model.RecentCampaignPresentation
import com.ecosense.android.featProfile.presentation.model.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
    private var mUserId by mutableStateOf<Int?>(null)

    var campaigns by mutableStateOf(emptyList<RecentCampaignPresentation>())
        private set

    var isLoading by mutableStateOf(false)
        private set

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    fun setUserId(userId: Int?) {
        mUserId = userId
        refresh()
    }

    private var refreshJob: Job? = null
    private fun refresh() {
        refreshJob?.cancel()
        refreshJob =  viewModelScope.launch {
            profileRepository.getCampaignsHistory(
                userId = mUserId,
            ).onEach { result ->
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