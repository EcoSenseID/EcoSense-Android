package com.ecosense.android.featReward.data.model

import com.ecosense.android.featReward.domain.model.RewardDetail

data class RewardDetailDto(
    val termsCondition: List<String>?,
    val isRedeemed: Boolean?,
    val bannerUrl: String?,
    val description: String?,
    val id: Int?,
    val validity: String?,
    val error: Boolean?,
    val message: String?,
    val title: String?,
    val pointsNeeded: Int?,
    val howToUse: List<String>?
) {
    fun toRewardDetail() = RewardDetail(
        termsCondition = termsCondition ?: emptyList(),
        isRedeemed = isRedeemed ?: false,
        bannerUrl = bannerUrl ?: "",
        description = description ?: "",
        id = id ?: 0,
        validity = validity ?: "",
        title = title ?: "",
        pointsNeeded = pointsNeeded ?: 0,
        howToUse = howToUse ?: emptyList()
    )
}