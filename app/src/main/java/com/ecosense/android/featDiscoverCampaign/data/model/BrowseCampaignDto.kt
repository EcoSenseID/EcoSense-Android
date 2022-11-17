package com.ecosense.android.featDiscoverCampaign.data.model

import com.ecosense.android.core.domain.model.Campaign
import com.ecosense.android.featDiscoverCampaign.domain.model.BrowseCategory

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
    val startDate: String?,
    val categories: List<BrowseCategoriesItem>?,
    val participantsCount: Int?,
    val isTrending: Boolean?,
    val isNew: Boolean?
) {
    fun toCampaign() = Campaign(
        id = id ?: 0,
        posterUrl = posterUrl ?: "",
        title = title ?: "",
        endDate = endDate ?: "",
        startDate = startDate ?: "",
        categories = categories?.map { it.toBrowseCategory() } ?: emptyList(),
        participantsCount = participantsCount ?: 0,
        isTrending = isTrending ?: false,
        isNew = isNew ?: false,
    )
}

data class BrowseCategoriesItem(
    val name: String?,
    val colorHex: String?
) {
    fun toBrowseCategory() = BrowseCategory(
        name = name ?: "",
        colorHex = colorHex ?: ""
    )
}

