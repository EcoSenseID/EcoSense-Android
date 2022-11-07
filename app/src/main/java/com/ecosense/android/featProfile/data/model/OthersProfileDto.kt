package com.ecosense.android.featProfile.data.model

import com.ecosense.android.core.data.model.StoryDto
import com.ecosense.android.featProfile.domain.model.OthersProfile

data class OthersProfileDto(
    val userId: Int?,
    val error: Boolean?,
    val message: String?,
    val name: String?,
    val avatarUrl: String?,
    val recentStories: List<StoryDto>?,
    val recentCampaigns: List<RecentCampaignDto>?
) {
    fun toDomain() = OthersProfile(
        userId = this.userId ?: 0,
        name = this.name ?: "",
        avatarUrl = this.avatarUrl ?: "",
        recentCampaigns = this.recentCampaigns?.map { it.toDomain() } ?: emptyList(),
        recentStories = this.recentStories?.map { it.toDomain() } ?: emptyList(),
    )
}