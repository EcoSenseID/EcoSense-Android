package com.ecosense.android.featForums.data.model

import com.ecosense.android.core.data.model.SharedCampaignDto
import com.ecosense.android.core.domain.model.Story

data class GetStoryDetailDto(
    val attachedPhotoUrl: String?,
    val avatarUrl: String?,
    val caption: String?,
    val createdAt: Long?,
    val error: Boolean?,
    val id: Int?,
    val isSupported: Boolean?,
    val message: String?,
    val name: String?,
    val repliesCount: Int?,
    val sharedCampaign: SharedCampaignDto?,
    val supportersAvatarsUrl: List<String>?,
    val supportersCount: Int?,
) {
    fun toDomain() = Story(
        id = this.id ?: 0,
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