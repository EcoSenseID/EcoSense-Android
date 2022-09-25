package com.ecosense.android.featReward.presentation.homepage

import com.ecosense.android.featReward.domain.model.RewardHomepage

data class RewardHomepageScreenState(
    val rewardHomepage: RewardHomepage,
    val isLoadingRewardHomepage: Boolean
) {
    companion object {
        val defaultValue = RewardHomepageScreenState(
            rewardHomepage = RewardHomepage(
                totalPoints = 300,
                donationRewards = emptyList(),
                hotDealsRewards = emptyList()
            ),
            isLoadingRewardHomepage = false
        )
    }
}