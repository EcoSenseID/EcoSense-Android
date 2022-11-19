package com.ecosense.android.featReward.data.model

import com.ecosense.android.featReward.domain.model.DonationRewards
import com.ecosense.android.featReward.domain.model.RewardHomepage
import com.ecosense.android.featReward.domain.model.WalletRewards
import com.google.gson.annotations.SerializedName

data class RewardHomepageDto(
    @SerializedName("totalPoints") val totalPoints: Int?,
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("walletRewards") val walletRewards: List<WalletRewardsDto>?,
    @SerializedName("donationRewards") val donationRewards: List<DonationRewardsDto>?
) {
    fun toRewardHomepage() = RewardHomepage(
        totalPoints = totalPoints ?: 0,
        walletRewards = walletRewards?.map { it.toWalletRewards() } ?: emptyList(),
        donationRewards = donationRewards?.map { it.toDonationRewards() } ?: emptyList()
    )
}

data class WalletRewardsDto(
    @SerializedName("partner") val partner: String?,
    @SerializedName("bannerUrl") val bannerUrl: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("pointsNeeded") val pointsNeeded: Int?
) {
    fun toWalletRewards() = WalletRewards(
        partner = partner ?: "",
        bannerUrl = bannerUrl ?: "",
        id = id ?: 0,
        title = title ?: "",
        pointsNeeded = pointsNeeded ?: 0
    )
}

data class DonationRewardsDto(
    @SerializedName("partner") val partner: String?,
    @SerializedName("bannerUrl") val bannerUrl: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("pointsNeeded") val pointsNeeded: Int?
) {
    fun toDonationRewards() = DonationRewards(
        partner = partner ?: "",
        bannerUrl = bannerUrl ?: "",
        id = id ?: 0,
        title = title ?: "",
        pointsNeeded = pointsNeeded ?: 0
    )
}
