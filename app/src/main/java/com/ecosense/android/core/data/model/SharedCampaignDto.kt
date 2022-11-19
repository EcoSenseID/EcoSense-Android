package com.ecosense.android.core.data.model

import com.ecosense.android.core.domain.model.SharedCampaign
import com.google.gson.annotations.SerializedName

data class SharedCampaignDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("posterUrl") val posterUrl: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("endAt") val endAt: Long?,
    @SerializedName("categories") val categories: List<CategoryDto>?,
    @SerializedName("participantsCount") val participantsCount: Int?,
    @SerializedName("isTrending") val isTrending: Boolean?,
    @SerializedName("isNew") val isNew: Boolean?
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