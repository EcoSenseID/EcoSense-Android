package com.ecosense.android.featProfile.data.model

import com.ecosense.android.core.data.model.StoryDto
import com.ecosense.android.featProfile.domain.model.OthersProfile
import com.google.gson.annotations.SerializedName

data class OthersProfileDto(
    @SerializedName("userId") val userId: Int?,
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("recentStories") val recentStories: List<StoryDto>?,
    @SerializedName("recentCampaigns") val recentCampaigns: List<RecentCampaignDto>?
) {
    fun toDomain() = OthersProfile(
        userId = this.userId ?: 0,
        name = this.name ?: "",
        avatarUrl = this.avatarUrl ?: "",
        recentCampaigns = this.recentCampaigns?.map { it.toDomain() } ?: emptyList(),
        recentStories = this.recentStories?.map { it.toDomain() } ?: emptyList(),
    )
}