package com.ecosense.android.featReward.presentation.detail.rewarddetail

import com.ecosense.android.featReward.domain.model.RewardDetail

data class RewardDetailScreenState(
    val email: String,
    val walletType: String,
    val walletNumber: String,
    val rewardDetail: RewardDetail,
    val isLoadingRewardDetail: Boolean,
    val isLoadingRedeemReward: Boolean,
    val isLoadingRequestReward: Boolean
) {
    companion object {
        val defaultValue = RewardDetailScreenState(
            email = "",
            walletType = "",
            walletNumber = "",
            rewardDetail = RewardDetail(
                termsCondition = emptyList(),
                bannerUrl = "",
                description = "",
                validity = "",
                title = "",
                partner = "",
                category = "",
                pointsNeeded = 0,
                maxRedeem = 0,
                numberOfRedeem = 0,
                howToUse = emptyList()
            ),
            isLoadingRewardDetail = false,
            isLoadingRedeemReward = false,
            isLoadingRequestReward = false
        )
    }
}