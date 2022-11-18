package com.ecosense.android.featDiscoverCampaign.data.model

import com.ecosense.android.featDiscoverCampaign.domain.model.BrowseCategory
import com.ecosense.android.featDiscoverCampaign.domain.model.CampaignDetail
import com.ecosense.android.featDiscoverCampaign.domain.model.CampaignMission

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
    val completionStatus: Int?,
    val earnedPoints: String?,
    val missions: List<MissionsItem>?,
    val categories: List<CampaignCategoriesItem>?
) {
    fun toCampaignDetails() = CampaignDetail(
        participantsCount = participantsCount ?: 0,
        title = title ?: "",
        posterUrl = posterUrl ?: "",
        isTrending = isTrending ?: false,
        isNew = isNew ?: false,
        initiator = initiator ?: "",
        startDate = startDate ?: "",
        endDate = endDate ?: "",
        description = description ?: "",
        joined = joined ?: false,
        completionStatus = completionStatus ?: 0,
        earnedPoints = earnedPoints ?: "",
        missions = missions?.map { it.toCampaignMission() } ?: emptyList(),
        categories = categories?.map { it.toBrowseCategory() } ?: emptyList()
    )
}

data class MissionsItem(
    val name: String?,
    val description: String?,
    val id: Int?,
    val completionStatus: Int?,
    val proofCaption: String?,
    val completedTimeStamp: String?,
    val proofPhotoUrl: String?
) {
    fun toCampaignMission() = CampaignMission(
        name = name ?: "",
        description = description ?: "",
        id = id ?: 0,
        completionStatus = completionStatus ?: 0,
        proofCaption = proofCaption ?: "",
        completedTimeStamp = completedTimeStamp ?: "",
        proofPhotoUrl = proofPhotoUrl ?: ""
    )
}

data class CampaignCategoriesItem(
    val name: String?,
    val colorHex: String?
) {
    fun toBrowseCategory() = BrowseCategory(
        name = name ?: "",
        colorHex = colorHex ?: ""
    )
}

