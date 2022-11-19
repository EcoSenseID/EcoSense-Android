package com.ecosense.android.featDiscoverCampaign.data.model

import com.ecosense.android.featDiscoverCampaign.domain.model.Category
import com.ecosense.android.featDiscoverCampaign.domain.model.DashboardCampaign
import com.ecosense.android.featDiscoverCampaign.domain.model.UncompletedMission
import com.google.gson.annotations.SerializedName

data class DashboardDto(
    @SerializedName("campaigns") val campaigns: List<DashboardCampaignsItem>?,
    @SerializedName("categories") val categories: List<DashboardCategoriesItem>?,
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
)

data class DashboardCategoriesItem(
    @SerializedName("photoUrl") val photoUrl: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("colorHex") val colorHex: String?,
) {
    fun toCategories() = Category(
        id = id ?: 0,
        photoUrl = photoUrl ?: "",
        name = name ?: "",
        colorHex = colorHex ?: "",
    )
}

data class DashboardCampaignsItem(
    @SerializedName("missionCompleted") val missionCompleted: Int?,
    @SerializedName("endDate") val endDate: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("uncompletedMissions") val uncompletedMissions: List<UncompletedMissionsItem>?,
    @SerializedName("id") val id: Int?,
    @SerializedName("completionStatus") val completionStatus: Int?,
    @SerializedName("missionLeft") val missionLeft: Int?,
) {
    fun toDashboardCampaign() = DashboardCampaign(
        missionCompleted = missionCompleted ?: 0,
        endDate = endDate ?: "",
        name = name ?: "",
        uncompletedMissions = uncompletedMissions?.map { it.toUncompletedMission() } ?: emptyList(),
        id = id ?: 0,
        completionStatus = completionStatus ?: 0,
        missionLeft = missionLeft ?: 0,
    )
}

data class UncompletedMissionsItem(
    @SerializedName("name") val name: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("completionStatus") val completionStatus: Int?,
) {
    fun toUncompletedMission() = UncompletedMission(
        name = name ?: "",
        id = id ?: 0,
        completionStatus = completionStatus ?: 0,
    )
}