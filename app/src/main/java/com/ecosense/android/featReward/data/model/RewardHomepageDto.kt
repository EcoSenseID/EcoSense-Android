package com.ecosense.android.featReward.data.model

import com.ecosense.android.featReward.domain.model.DonationRewards
import com.ecosense.android.featReward.domain.model.HotDealsRewards
import com.ecosense.android.featReward.domain.model.RewardHomepage

data class RewardHomepageDto(
	val totalPoints: Int?,
	val error: Boolean?,
	val message: String?,
	val hotDealsRewards: List<HotDealsRewardsDto>?,
	val donationRewards: List<DonationRewardsDto>?
) {
	fun toRewardHomepage() = RewardHomepage(
		totalPoints = totalPoints ?: 0,
		hotDealsRewards = hotDealsRewards?.map { it.toHotDealsRewards() } ?: emptyList(),
		donationRewards = donationRewards?.map { it.toDonationRewards() } ?: emptyList()
	)
}

data class HotDealsRewardsDto(
	val partner: String?,
	val bannerUrl: String?,
	val id: Int?,
	val title: String?,
	val pointsNeeded: Int?
) {
	fun toHotDealsRewards() = HotDealsRewards(
		partner = partner ?: "",
		bannerUrl = bannerUrl ?: "",
		id = id ?: 0,
		title = title ?: "",
		pointsNeeded = pointsNeeded ?: 0
	)
}

data class DonationRewardsDto(
	val partner: String?,
	val bannerUrl: String?,
	val id: Int?,
	val title: String?,
	val pointsNeeded: Int?
) {
	fun toDonationRewards() = DonationRewards(
		partner = partner ?: "",
		bannerUrl = bannerUrl ?: "",
		id = id ?: 0,
		title = title ?: "",
		pointsNeeded = pointsNeeded ?: 0
	)
}
