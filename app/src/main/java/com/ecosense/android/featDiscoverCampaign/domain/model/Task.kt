package com.ecosense.android.featDiscoverCampaign.domain.model

data class Task(
    val id: Int,
    val name: String,
    val taskDescription: String,
    val completed: Boolean,
    val proofPhotoUrl: String,
    val proofCaption: String,
    val completedTimeStamp: String
)
