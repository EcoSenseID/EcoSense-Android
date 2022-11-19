package com.ecosense.android.featDiscoverCampaign.data.model

import com.google.gson.annotations.SerializedName

data class CompleteCampaignDto(
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
)

