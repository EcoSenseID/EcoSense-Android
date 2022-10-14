package com.ecosense.android.featProfile.data.model

import com.ecosense.android.featProfile.domain.model.FinishedCampaign

data class FinishedCampaignDto(
    val categories: List<String>?,
    val completedAt: Long?,
    val id: Int?,
    val posterUrl: String?,
    val title: String?
) {
    fun toDomain() = FinishedCampaign(
        categories = this.categories ?: emptyList(),
        completedAt = this.completedAt ?: 0,
        id = this.id ?: 0,
        posterUrl = this.posterUrl ?: "",
        title = this.title ?: "",
    )
}