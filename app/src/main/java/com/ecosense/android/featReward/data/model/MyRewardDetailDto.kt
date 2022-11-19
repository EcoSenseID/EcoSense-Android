package com.ecosense.android.featReward.data.model

import com.ecosense.android.featReward.domain.model.MyRewardDetail
import com.google.gson.annotations.SerializedName

data class MyRewardDetailDto(
    @SerializedName("termsCondition") val termsCondition: List<String>?,
    @SerializedName("bannerUrl") val bannerUrl: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("validity") val validity: String?,
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("partner") val partner: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("claimStatus") val claimStatus: Int?,
    @SerializedName("howToUse") val howToUse: List<String>?
) {
    fun toMyRewardDetail() = MyRewardDetail(
        termsCondition = termsCondition ?: emptyList(),
        bannerUrl = bannerUrl ?: "",
        description = description ?: "",
        validity = validity ?: "",
        title = title ?: "",
        partner = partner ?: "",
        category = category ?: "",
        claimStatus = claimStatus ?: 0,
        howToUse = howToUse ?: emptyList()
    )
}
