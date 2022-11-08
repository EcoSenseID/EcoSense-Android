package com.ecosense.android.featReward.domain.model

data class WalletRewards(
    val partner: String,
    val bannerUrl: String,
    val id: Int,
    val title: String,
    val pointsNeeded: Int
)