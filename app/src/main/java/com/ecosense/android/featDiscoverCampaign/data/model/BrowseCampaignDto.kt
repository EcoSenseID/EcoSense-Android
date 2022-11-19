package com.ecosense.android.featDiscoverCampaign.data.model

import com.ecosense.android.core.domain.model.Campaign
import com.ecosense.android.featDiscoverCampaign.domain.model.BrowseCategory
import com.google.gson.annotations.SerializedName

data class BrowseCampaignDto(
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("campaigns") val campaigns: List<CampaignsItem>?
)

data class CampaignsItem(
    @SerializedName("id") val id: Int?,
    @SerializedName("posterUrl") val posterUrl: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("endDate") val endDate: String?,
    @SerializedName("startDate") val startDate: String?,
    @SerializedName("categories") val categories: List<BrowseCategoriesItem>?,
    @SerializedName("participantsCount") val participantsCount: Int?,
    @SerializedName("isTrending") val isTrending: Boolean?,
    @SerializedName("isNew") val isNew: Boolean?
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
    @SerializedName("name") val name: String?,
    @SerializedName("colorHex") val colorHex: String?,
) {
    fun toBrowseCategory() = BrowseCategory(
        name = name ?: "", colorHex = colorHex ?: ""
    )
}