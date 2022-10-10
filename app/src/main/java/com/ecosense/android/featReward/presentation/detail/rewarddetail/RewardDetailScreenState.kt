package com.ecosense.android.featReward.presentation.detail.rewarddetail

import com.ecosense.android.featReward.domain.model.RewardDetail

data class RewardDetailScreenState(
    val rewardDetail: RewardDetail,
    val isLoadingRewardDetail: Boolean,
    val isLoadingRedeemReward: Boolean,
    val isLoadingUseReward: Boolean
) {
    companion object {
        val defaultValue = RewardDetailScreenState(
            rewardDetail = RewardDetail(
                termsCondition = emptyList(),
                bannerUrl = "",
                description = "",
                validity = "",
                title = "",
                partner = "",
                pointsNeeded = 0,
                maxRedeem = 0,
                numberOfRedeem = 0,
                howToUse = emptyList()
            ),
            isLoadingRewardDetail = false,
            isLoadingRedeemReward = false,
            isLoadingUseReward = false
        )
    }
}