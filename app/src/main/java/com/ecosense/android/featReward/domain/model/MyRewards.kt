package com.ecosense.android.featReward.domain.model

data class MyRewards(
    val partner: String,
    val bannerUrl: String,
    val id: Int,
    val claimStatus: Int,
    val title: String,
    val category: String
) {
    companion object {
        val defaultValue = MyRewards(
            partner = "",
            bannerUrl = "",
            id = 0,
            claimStatus = 0,
            title = "",
            category = ""
        )
    }
}
