package com.ecosense.android.featReward.domain.model

data class CategoryRewards(
    val partner: String,
    val bannerUrl: String,
    val numberOfRedeem: Int,
    val id: Int,
    val title: String,
    val maxRedeem: Int,
    val pointsNeeded: Int
)
