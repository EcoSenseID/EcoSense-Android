package com.ecosense.android.featDiscoverCampaign.presentation.dashboard

import com.ecosense.android.featDiscoverCampaign.domain.model.Category
import com.ecosense.android.featDiscoverCampaign.domain.model.Dashboard

data class DiscoverCampaignScreenState(
    val dashboard: Dashboard,
    val categories: List<Category>,
    val isLoadingDashboard: Boolean,
    val isLoadingCategories: Boolean
) {
    companion object {
        val defaultValue = DiscoverCampaignScreenState(
            dashboard = Dashboard.defaultValue,
            categories = emptyList(),
            isLoadingDashboard = false,
            isLoadingCategories = false
        )
    }
}
