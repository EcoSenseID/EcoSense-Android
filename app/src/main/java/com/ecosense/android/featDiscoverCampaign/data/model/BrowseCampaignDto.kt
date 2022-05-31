package com.ecosense.android.featDiscoverCampaign.data.model

data class BrowseCampaignDto(
    val error: Boolean? = null,
    val message: String? = null,
    val campaigns: List<CampaignsItem?>? = null
)

data class CampaignsItem(
    val id: Int? = null,
    val posterUrl: String? = null,
    val title: String? = null,
    val endDate: String? = null,
    val category: List<String?>? = null,
    val participantsCount: Int? = null,
    val isTrending: Boolean? = null,
    val isNew: Boolean? = null
)

