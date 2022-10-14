package com.ecosense.android.featProfile.data.model

import com.ecosense.android.core.domain.model.Story

data class PostedStoryDto(
    val attachedPhotoUrl: String?,
    val avatarUrl: String?,
    val caption: String?,
    val createdAt: Long?,
    val id: Int?,
    val isSupported: Boolean?,
    val name: String?,
    val repliesCount: Int?,
    val sharedCampaign: SharedCampaignDto?,
    val supportersAvatarsUrl: List<String>?,
    val supportersCount: Int?
) {
    fun toDomain() = Story(
        id = this.id ?: 0,
        name = this.name ?: "",
        avatarUrl = this.avatarUrl ?: "",
        caption = this.caption ?: "",
        attachedPhotoUrl = this.attachedPhotoUrl ?: "",
        sharedCampaign = this.sharedCampaign?.toCampaign(),
        createdAt = 0,
        supportersCount = 0,
        supportersAvatarsUrl = emptyList(),
        repliesCount = 0,
        isSupported = false,
    )
}