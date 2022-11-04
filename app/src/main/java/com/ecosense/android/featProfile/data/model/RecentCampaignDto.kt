package com.ecosense.android.featProfile.data.model

import com.ecosense.android.core.data.model.CategoryDto
import com.ecosense.android.featProfile.domain.model.RecentCampaign

data class RecentCampaignDto(
    val id: Int?,
    val posterUrl: String?,
    val title: String?,
    val earnedPoints: Int?,
    val finishedAt: Long?,
    val endAt: Long?,
    val completionStatus: Int?,
    val categories: List<CategoryDto>?
) {
    fun toDomain(): RecentCampaign = RecentCampaign(
        id = this.id ?: 0,
        posterUrl = this.posterUrl ?: "",
        title = this.title ?: "",
        earnedPoints = this.earnedPoints ?: 0,
        finishedAt = this.finishedAt ?: 0,
        endAt = this.endAt ?: 0,
        completionStatus = this.completionStatus ?: 0,
        categories = this.categories?.map { it.toDomain() } ?: emptyList(),
    )
}