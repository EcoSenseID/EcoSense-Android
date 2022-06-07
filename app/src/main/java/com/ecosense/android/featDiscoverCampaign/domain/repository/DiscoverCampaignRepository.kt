package com.ecosense.android.featDiscoverCampaign.domain.repository

import android.net.Uri
import com.ecosense.android.core.domain.model.Campaign
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.featDiscoverCampaign.domain.model.CampaignDetail
import com.ecosense.android.featDiscoverCampaign.domain.model.Category
import com.ecosense.android.featDiscoverCampaign.domain.model.Dashboard
import kotlinx.coroutines.flow.Flow

interface DiscoverCampaignRepository {
    fun getCampaigns(q: String?, categoryId: Int?): Flow<Resource<List<Campaign>>>

    fun getCampaignDetail(id: Int): Flow<Resource<CampaignDetail>>

    fun getCategories(): Flow<Resource<List<Category>>>

    fun getDashboard(): Flow<Resource<Dashboard>>

    fun setCompletionProof(
        photo: String?,
        caption: String?,
        taskId: Int
    ): Flow<SimpleResource>

    fun setJoinCampaign(
        campaignId: Int
    ): Flow<SimpleResource>

    fun setCompleteCampaign(
        campaignId: Int
    ): Flow<SimpleResource>

    suspend fun getNewTempJpegUri(): Uri
}