package com.ecosense.android.featReward.presentation.categoryrewards

import com.ecosense.android.featReward.domain.model.Rewards

data class CategoryRewardsScreenState(
    val rewards: Rewards,
    val isLoadingRewardList: Boolean
) {
    companion object {
        val defaultValue = CategoryRewardsScreenState(
            rewards = Rewards.defaultValue,
            isLoadingRewardList = false
        )
    }
}