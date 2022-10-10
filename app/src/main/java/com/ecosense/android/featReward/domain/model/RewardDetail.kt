package com.ecosense.android.featReward.domain.model

data class RewardDetail(
    val termsCondition: List<String>,
    val bannerUrl: String,
    val description: String,
    val validity: String,
    val title: String,
    val partner: String,
    val pointsNeeded: Int,
    val maxRedeem: Int,
    val numberOfRedeem: Int,
    val howToUse: List<String>
)