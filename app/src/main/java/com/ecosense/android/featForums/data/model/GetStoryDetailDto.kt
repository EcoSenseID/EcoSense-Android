package com.ecosense.android.featForums.data.model

import com.ecosense.android.core.data.model.SharedCampaignDto
import com.ecosense.android.core.domain.model.Story
import com.google.gson.annotations.SerializedName

data class GetStoryDetailDto(
    @SerializedName("attachedPhotoUrl") val attachedPhotoUrl: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("caption") val caption: String?,
    @SerializedName("createdAt") val createdAt: Long?,
    @SerializedName("error") val error: Boolean?,
    @SerializedName("id") val id: Int?,
    @SerializedName("userId") val userId: Int?,
    @SerializedName("isSupported") val isSupported: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("repliesCount") val repliesCount: Int?,
    @SerializedName("sharedCampaign") val sharedCampaign: SharedCampaignDto?,
    @SerializedName("supportersAvatarsUrl") val supportersAvatarsUrl: List<String>?,
    @SerializedName("supportersCount") val supportersCount: Int?,
) {
    fun toDomain() = Story(
        id = this.id ?: 0,
        userId = this.userId ?: 0,
        name = this.name ?: "",
        avatarUrl = this.avatarUrl ?: "",
        caption = this.caption ?: "",
        attachedPhotoUrl = this.attachedPhotoUrl,
        sharedCampaign = this.sharedCampaign?.toDomain(),
        createdAt = (this.createdAt ?: 0) * 1000,
        supportersCount = this.supportersCount ?: 0,
        supportersAvatarsUrl = this.supportersAvatarsUrl ?: emptyList(),
        repliesCount = this.repliesCount ?: 0,
        isSupported = this.isSupported ?: false,
    )
}