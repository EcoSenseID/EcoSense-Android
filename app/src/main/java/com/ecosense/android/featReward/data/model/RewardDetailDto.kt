package com.ecosense.android.featReward.data.model

import com.ecosense.android.featReward.domain.model.RewardDetail

data class RewardDetailDto(
    val termsCondition: List<String>?,
    val bannerUrl: String?,
    val description: String?,
    val validity: String?,
    val error: Boolean?,
    val message: String?,
    val title: String?,
    val partner: String?,
    val pointsNeeded: Int?,
    val maxRedeem: Int?,
    val numberOfRedeem: Int?,
    val howToUse: List<String>?
) {
    fun toRewardDetail() = RewardDetail(
        termsCondition = termsCondition ?: emptyList(),
        bannerUrl = bannerUrl ?: "",
        description = description ?: "",
        validity = validity ?: "",
        title = title ?: "",
        partner = partner ?: "",
        pointsNeeded = pointsNeeded ?: 0,
        maxRedeem = maxRedeem ?: 0,
        numberOfRedeem = numberOfRedeem ?: 0,
        howToUse = howToUse ?: emptyList()
    )
}