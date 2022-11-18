package com.ecosense.android.featDiscoverCampaign.domain.model

data class CampaignDetail(
    val participantsCount: Int,
    val title: String,
    val posterUrl: String,
    val isTrending: Boolean,
    val isNew: Boolean,
    val initiator: String,
    val startDate: String,
    val endDate: String,
    val description: String,
    val joined: Boolean,
    val completionStatus: Int,
    val earnedPoints: String,
    val missions: List<CampaignMission>,
    val categories: List<BrowseCategory>
)
