package com.ecosense.android.core.domain.model

data class Story(
    val id: Int,
    val userId: Int,
    val name: String,
    val avatarUrl: String,
    val caption: String,
    val attachedPhotoUrl: String?,
    val sharedCampaign: SharedCampaign?,
    val createdAt: Long,
    val supportersCount: Int,
    val supportersAvatarsUrl: List<String>,
    val repliesCount: Int,
    val isSupported: Boolean,
)