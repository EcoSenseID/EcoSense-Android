package com.ecosense.android.core.data.model

import com.ecosense.android.core.domain.model.Story

data class StoryDto(
    val id: Int?,
    val userId: Int?,
    val name: String?,
    val avatarUrl: String?,
    val caption: String?,
    val attachedPhotoUrl: String?,
    val sharedCampaign: SharedCampaignDto?,
    val createdAt: Long?,
    val supportersCount: Int?,
    val repliesCount: Int?,
    val isSupported: Boolean?,
    val supportersAvatarsUrl: List<String>?
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