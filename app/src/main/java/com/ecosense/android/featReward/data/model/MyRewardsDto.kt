package com.ecosense.android.featReward.data.model

import com.google.gson.annotations.SerializedName

data class MyRewardsDto(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("myRewards")
    val myRewards: List<MyRewardsItem?>? = null
)

data class MyRewardsItem(

    @field:SerializedName("partner")
    val partner: String? = null,

    @field:SerializedName("bannerUrl")
    val bannerUrl: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("category")
    val category: String? = null
)
