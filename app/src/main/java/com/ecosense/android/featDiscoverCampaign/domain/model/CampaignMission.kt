package com.ecosense.android.featDiscoverCampaign.domain.model

data class CampaignMission(
    val name: String,
    val description: String,
    val id: Int,
    val completionStatus: Int,
    val proofCaption: String,
    val completedTimeStamp: String,
    val proofPhotoUrl: String
)
