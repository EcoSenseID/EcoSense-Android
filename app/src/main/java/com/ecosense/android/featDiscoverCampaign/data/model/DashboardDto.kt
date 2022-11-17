package com.ecosense.android.featDiscoverCampaign.data.model

import com.ecosense.android.featDiscoverCampaign.domain.model.Category
import com.ecosense.android.featDiscoverCampaign.domain.model.DashboardCampaign
import com.ecosense.android.featDiscoverCampaign.domain.model.UncompletedMission

data class DashboardDto(
    val campaigns: List<DashboardCampaignsItem>?,
    val categories: List<DashboardCategoriesItem>?,
    val error: Boolean?,
    val message: String?
)

data class DashboardCategoriesItem(
    val photoUrl: String?,
    val name: String?,
    val id: Int?,
    val colorHex: String?
) {
    fun toCategories() = Category(
        id = id ?: 0,
        photoUrl = photoUrl ?: "",
        name = name ?: "",
        colorHex = colorHex ?: ""
    )
}

data class DashboardCampaignsItem(
    val missionCompleted: Int?,
    val endDate: String?,
    val name: String?,
    val uncompletedMissions: List<UncompletedMissionsItem>?,
    val id: Int?,
    val completionStatus: Int?,
    val missionLeft: Int?
) {
    fun toDashboardCampaign() = DashboardCampaign(
        missionCompleted = missionCompleted ?: 0,
        endDate = endDate ?: "",
        name = name ?: "",
        uncompletedMissions = uncompletedMissions?.map { it.toUncompletedMission() } ?: emptyList(),
        id = id ?: 0,
        completionStatus = completionStatus ?: 0,
        missionLeft = missionLeft ?: 0
    )
}

data class UncompletedMissionsItem(
    val name: String?,
    val id: Int?,
    val completionStatus: Int?
) {
    fun toUncompletedMission() = UncompletedMission(
        name = name ?: "",
        id = id ?: 0,
        completionStatus = completionStatus ?: 0
    )
}