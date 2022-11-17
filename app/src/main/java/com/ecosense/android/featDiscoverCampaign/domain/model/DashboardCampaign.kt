package com.ecosense.android.featDiscoverCampaign.domain.model

data class DashboardCampaign(
    val missionCompleted: Int,
    val endDate: String,
    val name: String,
    val uncompletedMissions: List<UncompletedMission>,
    val id: Int,
    val completionStatus: Int,
    val missionLeft: Int
)
