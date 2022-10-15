package com.ecosense.android.core.data.model

import com.ecosense.android.core.domain.model.SharedCampaign

data class SharedCampaignDto(
    val id: Int?,
    val posterUrl: String?,
    val title: String?,
    val endAt: Long?,
    val categories: List<CategoryDto>?,
    val participantsCount: Int?,
    val isTrending: Boolean?,
    val isNew: Boolean?
) {
    fun toDomain(): SharedCampaign = SharedCampaign(
        id = this.id ?: 0,
        posterUrl = this.posterUrl ?: "",
        title = this.title ?: "",
        endAt = this.endAt ?: 0,
        categories = this.categories?.map { it.toDomain() } ?: emptyList(),
        participantsCount = this.participantsCount ?: 0,
        isTrending = this.isTrending ?: false,
        isNew = this.isNew ?: false,
    )
}