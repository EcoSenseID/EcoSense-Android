package com.ecosense.android.featReward.data.model

import com.ecosense.android.featReward.domain.model.Rewards

data class RewardsDto(
    val rewards: List<RewardsItem>?,
    val error: Boolean?,
    val message: String?
)

data class RewardsItem(
    val partner: String?,
    val bannerUrl: String?,
    val numberOfRedeem: Int?,
    val id: Int?,
    val title: String?,
    val category: String?,
    val maxRedeem: Int?,
    val pointsNeeded: Int?
) {
    fun toRewards() = Rewards(
        partner = partner ?: "",
        bannerUrl = bannerUrl ?: "",
        numberOfRedeem = numberOfRedeem ?: 0,
        id = id ?: 0,
        title = title ?: "",
        category = category ?: "",
        maxRedeem = maxRedeem ?: 0,
        pointsNeeded = pointsNeeded ?: 0
    )
}
