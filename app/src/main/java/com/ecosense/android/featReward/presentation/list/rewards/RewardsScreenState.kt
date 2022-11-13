package com.ecosense.android.featReward.presentation.list.rewards

import com.ecosense.android.featReward.domain.model.Rewards

data class RewardsScreenState(
    val email: String,
    val walletType: String,
    val walletNumber: String,
    val sheetConditional: Int,
    val rewards: List<Rewards>,
    val isLoadingRewardList: Boolean,
    val isLoadingRedeemReward: Boolean,
    val isLoadingRequestReward: Boolean
) {
    companion object {
        val defaultValue = RewardsScreenState(
            email = "",
            walletType = "",
            walletNumber = "",
            sheetConditional = 1,
            rewards = emptyList(),
            isLoadingRewardList = false,
            isLoadingRedeemReward = false,
            isLoadingRequestReward = false
        )
    }
}