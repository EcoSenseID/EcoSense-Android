package com.ecosense.android.features.discoverCampaign.data.repository

import com.ecosense.android.core.util.Resource
import com.ecosense.android.features.discoverCampaign.domain.model.Campaign
import com.ecosense.android.features.discoverCampaign.domain.repository.DiscoverCampaignRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DiscoverCampaignRepositoryImpl : DiscoverCampaignRepository {
    override fun getCampaigns(): Flow<Resource<List<Campaign>>> = flow {
        // TODO: not yet implemented
    }
}