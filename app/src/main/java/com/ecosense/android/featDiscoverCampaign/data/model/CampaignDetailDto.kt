package com.ecosense.android.featDiscoverCampaign.data.model

import com.ecosense.android.featDiscoverCampaign.domain.model.BrowseCategory
import com.ecosense.android.featDiscoverCampaign.domain.model.CampaignDetail
import com.ecosense.android.featDiscoverCampaign.domain.model.CampaignMission
import com.google.gson.annotations.SerializedName

data class CampaignDetailDto(
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("participantsCount") val participantsCount: Int?,
    @SerializedName("category") val category: List<String>?,
    @SerializedName("title") val title: String?,
    @SerializedName("posterUrl") val posterUrl: String?,
    @SerializedName("isTrending") val isTrending: Boolean?,
    @SerializedName("isNew") val isNew: Boolean?,
    @SerializedName("initiator") val initiator: String?,
    @SerializedName("startDate") val startDate: String?,
    @SerializedName("endDate") val endDate: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("joined") val joined: Boolean?,
    @SerializedName("completionStatus") val completionStatus: Int?,
    @SerializedName("earnedPoints") val earnedPoints: String?,
    @SerializedName("missions") val missions: List<MissionsItem>?,
    @SerializedName("categories") val categories: List<CampaignCategoriesItem>?
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
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("completionStatus") val completionStatus: Int?,
    @SerializedName("proofCaption") val proofCaption: String?,
    @SerializedName("completedTimeStamp") val completedTimeStamp: String?,
    @SerializedName("proofPhotoUrl") val proofPhotoUrl: String?
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
    @SerializedName("name") val name: String?,
    @SerializedName("colorHex") val colorHex: String?
) {
    fun toBrowseCategory() = BrowseCategory(
        name = name ?: "",
        colorHex = colorHex ?: ""
    )
}

