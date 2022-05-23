package com.ecosense.android.featProfile.data.model

data class ContributionsDto(
    val error: Boolean?,
    val message: String?,
    val experiences: List<ExperienceDto>?,
    val completedCampaigns: List<CompletedCampaignDto>?
)