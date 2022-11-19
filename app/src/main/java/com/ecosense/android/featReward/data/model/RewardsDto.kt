package com.ecosense.android.featReward.data.model

import com.ecosense.android.featReward.domain.model.Rewards
import com.google.gson.annotations.SerializedName

data class RewardsDto(
    @SerializedName("rewards") val rewards: List<RewardsItem>?,
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
)

data class RewardsItem(
    @SerializedName("partner") val partner: String?,
    @SerializedName("bannerUrl") val bannerUrl: String?,
    @SerializedName("numberOfRedeem") val numberOfRedeem: Int?,
    @SerializedName("id") val id: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("maxRedeem") val maxRedeem: Int?,
    @SerializedName("pointsNeeded") val pointsNeeded: Int?,
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
