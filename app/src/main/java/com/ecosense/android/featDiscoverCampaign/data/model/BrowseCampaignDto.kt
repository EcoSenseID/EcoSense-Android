package com.ecosense.android.featDiscoverCampaign.data.model

import com.ecosense.android.core.domain.model.Campaign

data class BrowseCampaignDto(
    val error: Boolean?,
    val message: String?,
    val campaigns: List<CampaignsItem>?
)

data class CampaignsItem(
    val id: Int?,
    val posterUrl: String?,
    val title: String?,
    val endDate: String?,
    val category: List<String>?,
    val participantsCount: Int?,
    val isTrending: Boolean?,
    val isNew: Boolean?
) {
    fun toCampaign() = Campaign(
        id = id ?: 0,
        posterUrl = posterUrl ?: "",
        title = title ?: "",
        endDate = endDate ?: "",
        category = category ?: emptyList(),
        participantsCount = participantsCount ?: 0,
        isTrending = isTrending ?: false,
        isNew = isNew ?: false,
    )
}

