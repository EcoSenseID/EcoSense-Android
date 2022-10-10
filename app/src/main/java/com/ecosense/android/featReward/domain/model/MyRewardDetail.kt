package com.ecosense.android.featReward.domain.model

data class MyRewardDetail(
    val termsCondition: List<String>,
    val bannerUrl: String,
    val description: String,
    val validity: String,
    val title: String,
    val partner: String,
    val pointsNeeded: Int,
    val claimStatus: Int,
    val howToUse: List<String>
)