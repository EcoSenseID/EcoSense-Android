package com.ecosense.android.featDiscoverCampaign.presentation.detail

import com.ecosense.android.featDiscoverCampaign.domain.model.CampaignDetail

data class CampaignDetailScreenState(
    val campaignDetail: CampaignDetail,
    val isLoadingCampaignDetail: Boolean
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
            isLoadingCampaignDetail = false
        )
    }
}