package com.ecosense.android.featReward.domain.model

data class RewardDetail(
    val termsCondition: List<String>,
    val isRedeemed: Boolean,
    val bannerUrl: String,
    val description: String,
    val validity: String,
    val title: String,
    val pointsNeeded: Int,
    val howToUse: List<String>
)