package com.ecosense.android.featReward.domain.model

data class Rewards(
    val partner: String,
    val bannerUrl: String,
    val numberOfRedeem: Int,
    val id: Int,
    val title: String,
    val category: String,
    val maxRedeem: Int,
    val pointsNeeded: Int
) {
    companion object {
        val defaultValue = Rewards(
            partner = "",
            bannerUrl = "",
            numberOfRedeem = 0,
            id = 0,
            title = "",
            category = "",
            maxRedeem = 0,
            pointsNeeded = 0
        )
    }
}
