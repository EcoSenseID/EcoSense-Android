package com.ecosense.android.featDiscoverCampaign.presentation.detail

import com.ecosense.android.featDiscoverCampaign.domain.model.CampaignDetail

data class CampaignDetailScreenState(
    val campaignDetail: CampaignDetail,
    val proofPhotoUrl: String?,
    val isLoadingCampaignDetail: Boolean,
    val isLoadingUploadProof: Boolean,
    val isLoadingJoinCampaign: Boolean
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
                category = emptyList(),
                participantsCount = 0,
                isTrending = false,
                isNew = false,
                joined = false,
                campaignTasks = emptyList()
            ),
            proofPhotoUrl = null,
            isLoadingCampaignDetail = false,
            isLoadingUploadProof = false,
            isLoadingJoinCampaign = false
        )
    }
}