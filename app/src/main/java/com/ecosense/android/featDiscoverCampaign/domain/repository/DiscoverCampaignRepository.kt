package com.ecosense.android.featDiscoverCampaign.domain.repository

import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.domain.model.Campaign
import com.ecosense.android.featDiscoverCampaign.domain.model.CampaignDetail
import com.ecosense.android.featDiscoverCampaign.domain.model.Category
import com.ecosense.android.featDiscoverCampaign.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface DiscoverCampaignRepository {
    fun getCampaigns(): Flow<Resource<List<Campaign>>>

    fun getBrowseCampaign(): Flow<Resource<List<Campaign>>>

    fun getCampaignDetail(): Flow<Resource<List<CampaignDetail>>>

    fun getCategory(): Flow<Resource<List<Category>>>
}