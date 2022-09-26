package com.ecosense.android.featReward.presentation.rewards

import com.ecosense.android.featReward.domain.model.Rewards

data class RewardsScreenState(
    val rewards: List<Rewards>,
    val isLoadingRewardList: Boolean
) {
    companion object {
        val defaultValue = RewardsScreenState(
            rewards = emptyList(),
            isLoadingRewardList = false
        )
    }
}