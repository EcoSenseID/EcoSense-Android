package com.ecosense.android.featReward.data.model

import com.ecosense.android.featReward.domain.model.MyRewards

data class MyRewardsDto(
    val error: Boolean?,
    val message: String?,
    val myRewards: List<MyRewardsItem>?
)

data class MyRewardsItem(
    val partner: String?,
    val bannerUrl: String?,
    val id: Int?,
    val title: String?,
    val category: String?
) {
    fun toMyRewards() = MyRewards(
        partner = partner ?: "",
        bannerUrl = bannerUrl ?: "",
        id = id ?: 0,
        title = title ?: "",
        category = category ?: ""
    )
}
