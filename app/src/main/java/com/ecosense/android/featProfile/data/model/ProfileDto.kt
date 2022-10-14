package com.ecosense.android.featProfile.data.model

import com.ecosense.android.featProfile.domain.model.Profile

data class ProfileDto(
    val finishedCampaigns: List<FinishedCampaignDto>?,
    val error: Boolean?,
    val message: String?,
    val postedStories: List<PostedStoryDto>?,
    val totalEcoPoints: Int?
) {
    fun toDomain() = Profile(
        totalEcoPoints = this.totalEcoPoints ?: 0,
        postedStories = this.postedStories?.map { it.toDomain() } ?: emptyList(),
        finishedCampaigns = this.finishedCampaigns?.map { it.toDomain() } ?: emptyList(),
    )
}