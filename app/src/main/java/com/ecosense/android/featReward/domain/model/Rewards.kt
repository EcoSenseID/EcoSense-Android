package com.ecosense.android.featReward.domain.model

data class Rewards(
    val category: String,
    val categoryRewards: List<CategoryRewards>
) {
    companion object {
        val defaultValue = Rewards(
            category = "",
            categoryRewards = emptyList()
        )
    }
}
