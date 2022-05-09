package com.ecosense.android.featDiscoverCampaign.presentation

import androidx.lifecycle.ViewModel
import com.ecosense.android.featDiscoverCampaign.domain.repository.DiscoverCampaignRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiscoverCampaignViewModel @Inject constructor(
    private val repository: DiscoverCampaignRepository
) : ViewModel() {
    // TODO: not yet implemented
}