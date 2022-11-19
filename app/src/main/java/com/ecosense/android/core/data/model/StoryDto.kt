package com.ecosense.android.core.data.model

import com.ecosense.android.core.domain.model.Story
import com.google.gson.annotations.SerializedName

data class StoryDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("userId") val userId: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("caption") val caption: String?,
    @SerializedName("attachedPhotoUrl") val attachedPhotoUrl: String?,
    @SerializedName("sharedCampaign") val sharedCampaign: SharedCampaignDto?,
    @SerializedName("createdAt") val createdAt: Long?,
    @SerializedName("supportersCount") val supportersCount: Int?,
    @SerializedName("repliesCount") val repliesCount: Int?,
    @SerializedName("isSupported") val isSupported: Boolean?,
    @SerializedName("supportersAvatarsUrl") val supportersAvatarsUrl: List<String>?
) {
    fun toDomain(): Story = Story(
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