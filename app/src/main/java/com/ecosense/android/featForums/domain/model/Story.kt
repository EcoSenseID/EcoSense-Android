package com.ecosense.android.featForums.domain.model

import com.ecosense.android.core.domain.model.Campaign

data class Story(
    val id: Int,
    val name: String,
    val username: String,
    val avatarUrl: String,
    val caption: String,
    val attachedPhotoUrl: String?,
    val sharedCampaign: Campaign?,
    val createdAt: Long,
    val supportersCount: Int,
    val supportersAvatarsUrl: List<String>,
    val repliesCount: Int,
    val isSupported: Boolean,
)