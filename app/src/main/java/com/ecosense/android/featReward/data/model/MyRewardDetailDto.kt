package com.ecosense.android.featReward.data.model

import com.google.gson.annotations.SerializedName

data class MyRewardDetailDto(

    @field:SerializedName("termsCondition")
    val termsCondition: List<String?>? = null,

    @field:SerializedName("isRedeemed")
    val isRedeemed: Boolean? = null,

    @field:SerializedName("bannerUrl")
    val bannerUrl: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("validity")
    val validity: String? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("pointsNeeded")
    val pointsNeeded: Int? = null,

    @field:SerializedName("howToUse")
    val howToUse: List<String?>? = null
)
