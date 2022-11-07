package com.ecosense.android.featProfile.presentation.othersProfile

import com.ecosense.android.core.domain.model.Story
import com.ecosense.android.featProfile.domain.model.OthersProfile
import com.ecosense.android.featProfile.domain.model.RecentCampaign
import com.ecosense.android.featProfile.presentation.model.RecentCampaignPresentation
import com.ecosense.android.featProfile.presentation.model.toPresentation

data class OthersProfilePresentation(
    val userId: Int,
    val name: String,
    val avatarUrl: String,
    val recentCampaigns: List<RecentCampaignPresentation>,
    val recentStories: List<Story>,
) {
    companion object {
        val defaultValue = OthersProfilePresentation(
            userId = 0,
            name = "",
            avatarUrl = "",
            recentCampaigns = emptyList(),
            recentStories = emptyList(),
        )
    }
}

fun OthersProfile.toPresentation() = OthersProfilePresentation(
    userId = this.userId,
    name = this.name,
    avatarUrl = this.avatarUrl,
    recentCampaigns = this.recentCampaigns.map { it.toPresentation() },
    recentStories = this.recentStories,
)