package com.ecosense.android.featDiscoverCampaign.domain.model

data class CampaignDetail(
    val id: Int,
    val posterUrl: String,
    val initiator: String,
    val title: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val category: List<String>,
    val participantsCount: Int,
    val isTrending: Boolean,
    val isNew: Boolean,
    val isJoined: Boolean,
    val tasks: List<Task>
)
