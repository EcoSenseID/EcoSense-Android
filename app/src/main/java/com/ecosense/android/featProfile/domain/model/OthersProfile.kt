package com.ecosense.android.featProfile.domain.model

import com.ecosense.android.core.domain.model.Story

data class OthersProfile(
    val userId: Int,
    val name: String,
    val avatarUrl: String,
    val recentCampaigns: List<RecentCampaign>,
    val recentStories: List<Story>,
)