package com.ecosense.android.featDiscoverCampaign.data.repository

import com.ecosense.android.core.util.Resource
import com.ecosense.android.featDiscoverCampaign.data.util.Faker
import com.ecosense.android.featDiscoverCampaign.domain.model.Campaign
import com.ecosense.android.featDiscoverCampaign.domain.repository.DiscoverCampaignRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DiscoverCampaignRepositoryImpl : DiscoverCampaignRepository {
    override fun getCampaigns(): Flow<Resource<List<Campaign>>> = flow {
        // TODO: not yet implemented
    }

    override fun getBrowseCampaign(): Flow<Resource<List<Campaign>>> = flow {
        emit(Resource.Loading())

        // Request api di sini
        val response = Faker.getBrowseCampaign()

        emit(Resource.Success(response))
    }
}