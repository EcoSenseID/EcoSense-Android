package com.ecosense.android.featReward.domain.model

data class RewardHomepage(
    val totalPoints: Int,
    val hotDealsRewards: List<HotDealsRewards>,
    val donationRewards: List<DonationRewards>
)
