package com.ecosense.android.featDiscoverCampaign.domain.repository

import com.ecosense.android.core.util.Resource
import com.ecosense.android.featDiscoverCampaign.domain.model.Campaign
import kotlinx.coroutines.flow.Flow

interface DiscoverCampaignRepository {
    fun getCampaigns(): Flow<Resource<List<Campaign>>>
}