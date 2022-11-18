package com.ecosense.android.featDiscoverCampaign.presentation.detail

import android.net.Uri
import com.ecosense.android.featDiscoverCampaign.domain.model.CampaignDetail

data class CampaignDetailScreenState(
    val campaignDetail: CampaignDetail,
    val allMissionIsReadyToSend: Boolean,
    val proofPhotoUrl: String?,
    val isLoadingCampaignDetail: Boolean,
    val isLoadingUploadProof: Boolean,
    val isLoadingJoinCampaign: Boolean,
    val isLoadingCompleteCampaign: Boolean,
    val tempJpegUri: Uri?,
) {
    companion object {
        val defaultValue = CampaignDetailScreenState(
            campaignDetail = CampaignDetail(
                posterUrl = "",
                initiator = "",
                title = "",
                description = "",
                startDate = "",
                endDate = "",
                participantsCount = 0,
                isTrending = false,
                isNew = false,
                joined = false,
                completionStatus = 0,
                earnedPoints = "",
                missions = emptyList(),
                categories = emptyList()
            ),
            allMissionIsReadyToSend = false,
            proofPhotoUrl = null,
            isLoadingCampaignDetail = false,
            isLoadingUploadProof = false,
            isLoadingJoinCampaign = false,
            isLoadingCompleteCampaign = false,
            tempJpegUri = null
        )
    }
}