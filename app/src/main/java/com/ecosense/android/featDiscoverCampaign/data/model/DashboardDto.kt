package com.ecosense.android.featDiscoverCampaign.data.model

import com.ecosense.android.core.domain.model.Campaign
import com.ecosense.android.featDiscoverCampaign.domain.model.DashboardTask

data class DashboardDto(
    val error: Boolean?,
    val message: String?,
    val tasks: List<DashboardTasksItem>?,
    val completedCampaigns: List<CompletedCampaignsItem>?
)

data class DashboardTasksItem(
    val id: Int?,
    val campaignId: Int?,
    val name: String?,
    val campaignName: String?,
    val campaignEndDate: String?,
    val tasksLeft: Int?,
    val completed: Boolean?
) {
    fun toDashboardTask() = DashboardTask(
        id = id ?: 0,
        campaignId = campaignId ?: 0,
        name = name ?: "",
        campaignName = campaignName ?: "",
        campaignEndDate = campaignEndDate ?: "",
        tasksLeft = tasksLeft ?: 0,
        completed = completed ?: false
    )
}

data class CompletedCampaignsItem(
    val id: Int?,
    val posterUrl: String?,
    val title: String?,
    val endDate: String?,
    val category: List<String>?,
    val participantsCount: Int?,
    val isTrending: Boolean?,
    val isNew: Boolean?
) {
    fun toCampaign() = Campaign(
        id = id ?: 0,
        posterUrl = posterUrl ?: "",
        title = title ?: "",
        endDate = endDate ?: "",
        category = category ?: emptyList(),
        participantsCount = participantsCount ?: 0,
        isTrending = isTrending ?: false,
        isNew = isNew ?: false,
    )
}

