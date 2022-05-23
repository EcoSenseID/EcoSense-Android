package com.ecosense.android.featProfile.data.model

import com.ecosense.android.core.domain.model.Campaign

data class CompletedCampaignDto(
    val id: Int?,
    val posterUrl: String?,
    val title: String?,
    val endDate: String?,
    val category: List<String>?,
    val participantsCount: Int?,
    val isNew: Boolean?,
    val isTrending: Boolean?,
) {
    fun toCampaign() = Campaign(
        id = id ?: 0,
        posterUrl = posterUrl ?: "",
        title = title ?: "",
        endDate = endDate ?: "",
        category = category ?: emptyList(),
        participantsCount = participantsCount ?: 0,
        isTrending = isTrending ?: false,
        isNew = isNew ?: false,
    )
}