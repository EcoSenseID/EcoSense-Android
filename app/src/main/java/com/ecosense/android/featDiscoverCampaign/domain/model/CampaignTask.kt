package com.ecosense.android.featDiscoverCampaign.domain.model

data class CampaignTask(
    val id: Int,
    val name: String,
    val completed: Boolean,
    val proofPhotoUrl: String,
    val proofCaption: String,
    val completedTimeStamp: String
)
