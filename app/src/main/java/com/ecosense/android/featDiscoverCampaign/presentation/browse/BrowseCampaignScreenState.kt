package com.ecosense.android.featDiscoverCampaign.presentation.browse

import com.ecosense.android.core.domain.model.Campaign

data class BrowseCampaignScreenState(
    val campaigns: List<Campaign>,
    val isLoadingCampaigns: Boolean
) {
    companion object {
        val defaultValue = BrowseCampaignScreenState(
            campaigns = emptyList(),
            isLoadingCampaigns = false
        )
    }
}