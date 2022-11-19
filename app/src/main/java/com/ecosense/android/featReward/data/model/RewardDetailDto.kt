package com.ecosense.android.featReward.data.model

import com.ecosense.android.featReward.domain.model.RewardDetail
import com.google.gson.annotations.SerializedName

data class RewardDetailDto(
    @SerializedName("termsCondition") val termsCondition: List<String>?,
    @SerializedName("bannerUrl") val bannerUrl: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("validity") val validity: String?,
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("partner") val partner: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("pointsNeeded") val pointsNeeded: Int?,
    @SerializedName("maxRedeem") val maxRedeem: Int?,
    @SerializedName("numberOfRedeem") val numberOfRedeem: Int?,
    @SerializedName("howToUse") val howToUse: List<String>?
) {
    fun toRewardDetail() = RewardDetail(
        termsCondition = termsCondition ?: emptyList(),
        bannerUrl = bannerUrl ?: "",
        description = description ?: "",
        validity = validity ?: "",
        title = title ?: "",
        partner = partner ?: "",
        category = category ?: "",
        pointsNeeded = pointsNeeded ?: 0,
        maxRedeem = maxRedeem ?: 0,
        numberOfRedeem = numberOfRedeem ?: 0,
        howToUse = howToUse ?: emptyList()
    )
}