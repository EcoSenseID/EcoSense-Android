package com.ecosense.android.featDiscoverCampaign.data.model

data class DashboardDto(
    val error: Boolean? = null,
    val message: String? = null,
    val tasks: List<DashboardTasksItem?>? = null,
    val completedCampaigns: List<CompletedCampaignsItem?>? = null
)

data class DashboardTasksItem(
    val id: Int? = null,
    val campaignId: Int? = null,
    val name: String? = null,
    val campaignName: String? = null,
    val campaignEndDate: String? = null,
    val tasksLeft: Int? = null,
    val completed: Boolean? = null
)

data class CompletedCampaignsItem(
    val id: Int? = null,
    val posterUrl: String? = null,
    val title: String? = null,
    val endDate: String? = null,
    val category: List<String?>? = null,
    val participantsCount: Int? = null,
    val isTrending: Boolean? = null,
    val isNew: Boolean? = null
)

