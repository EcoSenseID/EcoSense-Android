package com.ecosense.android.featProfile.presentation.profile.model

import com.ecosense.android.core.domain.model.User
import com.ecosense.android.featForums.presentation.model.StoryPresentation

data class ProfileScreenState(
    val user: User,
    val isDropdownMenuExpanded: Boolean,
    val isLoading: Boolean,
    val totalEcoPoints: Int,
    val recentStories: List<StoryPresentation>,
    val recentCampaigns: List<RecentCampaignPresentation>,
) {
    companion object {
        val defaultValue = ProfileScreenState(
            user = User.defaultValue,
            isDropdownMenuExpanded = false,
            isLoading = false,
            totalEcoPoints = 0,
            recentStories = emptyList(),
            recentCampaigns = emptyList(),
        )
    }
}