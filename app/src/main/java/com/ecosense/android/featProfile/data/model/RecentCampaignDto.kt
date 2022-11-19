package com.ecosense.android.featProfile.data.model

import com.ecosense.android.core.data.model.CategoryDto
import com.ecosense.android.featProfile.domain.model.RecentCampaign
import com.google.gson.annotations.SerializedName

data class RecentCampaignDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("posterUrl") val posterUrl: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("earnedPoints") val earnedPoints: Int?,
    @SerializedName("finishedAt") val finishedAt: Long?,
    @SerializedName("endAt") val endAt: Long?,
    @SerializedName("completionStatus") val completionStatus: Int?,
    @SerializedName("categories") val categories: List<CategoryDto>?
) {
    fun toDomain(): RecentCampaign = RecentCampaign(
        id = this.id ?: 0,
        posterUrl = this.posterUrl ?: "",
        title = this.title ?: "",
        earnedPoints = this.earnedPoints ?: 0,
        finishedAt = (this.finishedAt ?: 0) * 1000,
        endAt = (this.endAt ?: 0) * 1000,
        completionStatus = this.completionStatus ?: 0,
        categories = this.categories?.map { it.toDomain() } ?: emptyList(),
    )
}