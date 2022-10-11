package com.ecosense.android.featReward.presentation.list.rewards

import com.ecosense.android.featReward.domain.model.Rewards

data class RewardsScreenState(
    val rewards: List<Rewards>,
    val isLoadingRewardList: Boolean,
    val isLoadingRedeemReward: Boolean
) {
    companion object {
        val defaultValue = RewardsScreenState(
            rewards = emptyList(),
            isLoadingRewardList = false,
            isLoadingRedeemReward = false
        )
    }
}