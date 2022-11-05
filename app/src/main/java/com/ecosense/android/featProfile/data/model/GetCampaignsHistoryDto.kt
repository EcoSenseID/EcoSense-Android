package com.ecosense.android.featProfile.data.model

data class GetCampaignsHistoryDto(
    val error: Boolean?,
    val message: String?,
    val campaigns: List<RecentCampaignDto>?,
)