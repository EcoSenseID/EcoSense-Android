package com.ecosense.android.featProfile.domain.model

import com.ecosense.android.core.domain.model.Category

data class RecentCampaign(
    val id: Int,
    val posterUrl: String,
    val title: String,
    val earnedPoints: Int,
    val finishedAt: Long,
    val endAt: Long,
    val completionStatus: Int,
    val categories: List<Category>
)