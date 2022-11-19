package com.ecosense.android.featProfile.data.model

import com.google.gson.annotations.SerializedName

data class GetCampaignsHistoryDto(
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("campaigns") val campaigns: List<RecentCampaignDto>?,
)