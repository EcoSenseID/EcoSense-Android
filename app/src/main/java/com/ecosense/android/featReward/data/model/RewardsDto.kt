package com.ecosense.android.featReward.data.model

import com.ecosense.android.featReward.domain.model.CategoryRewards
import com.ecosense.android.featReward.domain.model.Rewards

data class RewardsDto(
    val categoryRewards: List<CategoryRewardsDto>?,
    val error: Boolean?,
    val message: String?,
    val category: String?
) {
    fun toRewards() = Rewards(
        category = category ?: "",
        categoryRewards = categoryRewards?.map { it.toCategoryRewards() } ?: emptyList()
    )
}

data class CategoryRewardsDto(
    val partner: String?,
    val bannerUrl: String?,
    val numberOfRedeem: Int?,
    val id: Int?,
    val title: String?,
    val maxRedeem: Int?,
    val pointsNeeded: Int?
) {
    fun toCategoryRewards() = CategoryRewards(
        partner = partner ?: "",
        bannerUrl = bannerUrl ?: "",
        numberOfRedeem = numberOfRedeem ?: 0,
        id = id ?: 0,
        title = title ?: "",
        maxRedeem = maxRedeem ?: 0,
        pointsNeeded = pointsNeeded ?: 0
    )
}
