package com.ecosense.android.featDiscoverCampaign.data.model

data class CampaignDetailDto(
    val error: Boolean? = null,
    val message: String? = null,
    val participantsCount: Int? = null,
    val category: List<String?>? = null,
    val title: String? = null,
    val posterUrl: String? = null,
    val isTrending: Boolean? = null,
    val isNew: Boolean? = null,
    val initiator: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val description: String? = null,
    val joined: Boolean? = null,
    val tasks: List<CampaignTasksItem?>? = null
)

data class CampaignTasksItem(
    val id: Int? = null,
    val name: String? = null,
    val taskDescription: String? = null,
    val completed: Boolean? = null,
    val proofPhotoUrl: String? = null,
    val proofCaption: String? = null,
    val completedTimeStamp: String? = null
)

