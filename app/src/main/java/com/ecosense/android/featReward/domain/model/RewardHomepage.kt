package com.ecosense.android.featReward.domain.model

data class RewardHomepage(
    val totalPoints: Int,
    val walletRewards: List<WalletRewards>,
    val donationRewards: List<DonationRewards>
)
