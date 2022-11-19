package com.ecosense.android.featProfile.data.model

import com.ecosense.android.core.data.model.StoryDto
import com.ecosense.android.featProfile.domain.model.Profile
import com.google.gson.annotations.SerializedName

data class ProfileDto(
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("totalEcoPoints") val totalEcoPoints: Int?,
    @SerializedName("recentStories") val recentStories: List<StoryDto>?,
    @SerializedName("recentCampaigns") val recentCampaigns: List<RecentCampaignDto>?
) {
    fun toDomain(): Profile = Profile(
        totalEcoPoints = this.totalEcoPoints ?: 0,
        recentCampaigns = this.recentCampaigns?.map { it.toDomain() } ?: emptyList(),
        recentStories = this.recentStories?.map { it.toDomain() } ?: emptyList(),
    )
}