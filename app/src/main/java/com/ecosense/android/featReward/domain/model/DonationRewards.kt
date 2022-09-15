package com.ecosense.android.featReward.domain.model

data class DonationRewards(
    val partner: String,
    val bannerUrl: String,
    val id: Int,
    val title: String,
    val pointsNeeded: Int
)