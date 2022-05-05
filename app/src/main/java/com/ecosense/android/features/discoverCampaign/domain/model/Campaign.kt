package com.ecosense.android.features.discoverCampaign.domain.model

data class Campaign(
    val name: String,
    val startDate: Long,
    val endDate: Long,
    val participantsCount: Int
)