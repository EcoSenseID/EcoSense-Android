package com.ecosense.android.featReward.data.model

import com.google.gson.annotations.SerializedName

data class RewardsDto(

    @field:SerializedName("categoryRewards")
    val categoryRewards: List<CategoryRewardsItem?>? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("category")
    val category: String? = null
)

data class CategoryRewardsItem(

    @field:SerializedName("partner")
    val partner: String? = null,

    @field:SerializedName("bannerUrl")
    val bannerUrl: String? = null,

    @field:SerializedName("numberOfRedeem")
    val numberOfRedeem: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("maxRedeem")
    val maxRedeem: Int? = null,

    @field:SerializedName("pointsNeeded")
    val pointsNeeded: Int? = null
)
