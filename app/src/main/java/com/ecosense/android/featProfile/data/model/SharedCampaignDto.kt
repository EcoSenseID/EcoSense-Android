package com.ecosense.android.featProfile.data.model

import com.ecosense.android.core.domain.model.Campaign

data class SharedCampaignDto(
    val category: List<String>?,
    val endDate: String?,
    val id: Int?,
    val isNew: Boolean?,
    val isTrending: Boolean?,
    val participantsCount: Int?,
    val posterUrl: String?,
    val title: String?
) {
    fun toCampaign() = Campaign(
        id = this.id ?: 0,
        posterUrl = this.posterUrl ?: "",
        title = this.title ?: "",
        endDate = this.endDate ?: "",
        category = this.category ?: emptyList(),
        participantsCount = this.participantsCount ?: 0,
        isTrending = false,
        isNew = false,
    )
}