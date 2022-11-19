package com.ecosense.android.featReward.data.model

import com.ecosense.android.featReward.domain.model.MyRewards
import com.google.gson.annotations.SerializedName

data class MyRewardsDto(
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("myRewards") val myRewards: List<MyRewardsItem>?
)

data class MyRewardsItem(
    @SerializedName("partner") val partner: String?,
    @SerializedName("bannerUrl") val bannerUrl: String?,
    @SerializedName("claimId") val claimId: Int?,
    @SerializedName("claimStatus") val claimStatus: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("category") val category: String?
) {
    fun toMyRewards() = MyRewards(
        partner = partner ?: "",
        bannerUrl = bannerUrl ?: "",
        claimId = claimId ?: 0,
        claimStatus = claimStatus ?: 0,
        title = title ?: "",
        category = category ?: ""
    )
}
