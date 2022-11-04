package com.ecosense.android.featProfile.data.model

import com.ecosense.android.core.data.model.StoryDto
import com.ecosense.android.featProfile.domain.model.Profile

data class ProfileDto(
    val error: Boolean?,
    val message: String?,
    val totalEcoPoints: Int?,
    val recentStories: List<StoryDto>?,
    val recentCampaigns: List<RecentCampaignDto>?
) {
    fun toDomain(): Profile = Profile(
        totalEcoPoints = this.totalEcoPoints ?: 0,
        recentCampaigns = this.recentCampaigns?.map { it.toDomain() } ?: emptyList(),
        recentStories = this.recentStories?.map { it.toDomain() } ?: emptyList(),
    )
}