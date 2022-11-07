package com.ecosense.android.featReward.presentation.homepage

import com.ecosense.android.core.domain.model.User
import com.ecosense.android.featReward.domain.model.RewardHomepage

data class RewardHomepageScreenState(
    val user: User,
    val rewardHomepage: RewardHomepage,
    val isLoadingRewardHomepage: Boolean
) {
    companion object {
        val defaultValue = RewardHomepageScreenState(
            user = User.defaultValue,
            rewardHomepage = RewardHomepage(
                totalPoints = 0,
                donationRewards = emptyList(),
                walletRewards = emptyList()
            ),
            isLoadingRewardHomepage = false
        )
    }
}