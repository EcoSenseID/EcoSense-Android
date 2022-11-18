package com.ecosense.android.featDiscoverCampaign.domain.model

data class Dashboard(
    val campaigns: List<DashboardCampaign>,
    val categories: List<Category>
) {
    companion object {
        val defaultValue = Dashboard(
            campaigns = emptyList(),
            categories = emptyList()
        )
    }
}
