package com.ecosense.android.featDiscoverCampaign.domain.model

data class DashboardTask(
    val id: Int,
    val campaignId: Int,
    val name: String,
    val campaignName: String,
    val campaignEndDate: String,
    val tasksLeft: Int,
    val completed: Boolean
)
