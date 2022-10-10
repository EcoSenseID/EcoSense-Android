package com.ecosense.android.featReward.data.model

import com.ecosense.android.featReward.domain.model.MyRewardDetail

data class MyRewardDetailDto(
	val termsCondition: List<String>?,
	val bannerUrl: String?,
	val description: String?,
	val validity: String?,
	val error: Boolean?,
	val message: String?,
	val title: String?,
	val partner: String?,
	val pointsNeeded: Int?,
	val claimStatus: Int?,
	val howToUse: List<String>?
) {
	fun toMyRewardDetail() = MyRewardDetail(
		termsCondition = termsCondition ?: emptyList(),
		bannerUrl = bannerUrl ?: "",
		description = description ?: "",
		validity = validity ?: "",
		title = title ?: "",
		partner = partner ?: "",
		pointsNeeded = pointsNeeded ?: 0,
		claimStatus = claimStatus ?: 0,
		howToUse = howToUse ?: emptyList()
	)
}
