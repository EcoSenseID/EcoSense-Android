package com.ecosense.android.featDiscoverCampaign.data.model

import com.ecosense.android.featDiscoverCampaign.domain.model.CampaignDetail
import com.ecosense.android.featDiscoverCampaign.domain.model.CampaignTask

data class CampaignDetailDto(
    val error: Boolean?,
    val message: String?,
    val participantsCount: Int?,
    val category: List<String>?,
    val title: String?,
    val posterUrl: String?,
    val isTrending: Boolean?,
    val isNew: Boolean?,
    val initiator: String?,
    val startDate: String?,
    val endDate: String?,
    val description: String?,
    val joined: Boolean?,
    val tasks: List<CampaignTasksDto>?
) {
    fun toCampaignDetails() = CampaignDetail(
        participantsCount = participantsCount ?: 0,
        category = category ?: emptyList(),
        title = title ?: "",
        posterUrl = posterUrl ?: "",
        isTrending = isTrending ?: false,
        isNew = isNew ?: false,
        initiator = initiator ?: "",
        startDate = startDate ?: "",
        endDate = endDate ?: "",
        description = description ?: "",
        joined = joined ?: false,
        campaignTasks = tasks?.map{ it.toCampaignTasks() } ?: emptyList()
    )
}

data class CampaignTasksDto(
    val id: Int?,
    val name: String?,
    val taskDescription: String?,
    val completed: Boolean?,
    val proofPhotoUrl: String?,
    val proofCaption: String?,
    val completedTimeStamp: String?
) {
    fun toCampaignTasks() = CampaignTask(
        id = id ?: 0,
        name = name ?: "nama campaign task",
        taskDescription = taskDescription ?: "",
        completed = completed ?: false,
        proofPhotoUrl = proofPhotoUrl ?: "",
        proofCaption = proofCaption ?: "",
        completedTimeStamp = completedTimeStamp ?: ""
    )
}

