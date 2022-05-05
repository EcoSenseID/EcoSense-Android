package com.ecosense.android.features.discoverCampaign.domain.repository

import com.ecosense.android.core.util.Resource
import com.ecosense.android.features.discoverCampaign.domain.model.Campaign
import kotlinx.coroutines.flow.Flow

interface DiscoverCampaignRepository {
    fun getCampaigns(): Flow<Resource<List<Campaign>>>
}