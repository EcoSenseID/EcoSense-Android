package com.ecosense.android.featDiscoverCampaign.domain.model

data class Campaign(
    val name: String,
    val startDate: Long,
    val endDate: Long,
    val participantsCount: Int
)