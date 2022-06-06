package com.ecosense.android.featDiscoverCampaign.domain.model

import com.ecosense.android.core.domain.model.Campaign

data class Dashboard(
    val tasks: List<DashboardTask>,
    val completedCampaigns: List<Campaign>,
) {
    companion object {
        val defaultValue = Dashboard(
            tasks = emptyList(),
            completedCampaigns = emptyList()
        )
    }
}
