package com.ecosense.android.featProfile.domain.model

data class FinishedCampaign(
    val categories: List<String>,
    val completedAt: Long,
    val id: Int,
    val posterUrl: String,
    val title: String,
)