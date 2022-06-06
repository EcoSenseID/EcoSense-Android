package com.ecosense.android.featDiscoverCampaign.data.repository

import com.ecosense.android.R
import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.core.domain.model.Campaign
import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.core.util.UIText
import com.ecosense.android.featDiscoverCampaign.data.api.DiscoverApi
import com.ecosense.android.featDiscoverCampaign.data.model.BrowseCampaignDto
import com.ecosense.android.featDiscoverCampaign.data.model.CampaignDetailDto
import com.ecosense.android.featDiscoverCampaign.data.model.CategoriesDto
import com.ecosense.android.featDiscoverCampaign.data.model.DashboardDto
import com.ecosense.android.featDiscoverCampaign.domain.model.CampaignDetail
import com.ecosense.android.featDiscoverCampaign.domain.model.Category
import com.ecosense.android.featDiscoverCampaign.domain.model.Dashboard
import com.ecosense.android.featDiscoverCampaign.domain.repository.DiscoverCampaignRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import logcat.asLog
import logcat.logcat
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class DiscoverCampaignRepositoryImpl(
    private val authApi: AuthApi,
    private val discoverApi: DiscoverApi
) : DiscoverCampaignRepository {
    override fun getCampaigns(q: String?, categoryId: Int?): Flow<Resource<List<Campaign>>> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = discoverApi.getCampaigns(bearerToken = bearerToken, q = q, categoryId = categoryId)

            when {
                response.error == true -> emit(Resource.Error(
                    uiText = response.message?.let { UIText.DynamicString(it) }
                        ?: UIText.StringResource(R.string.em_unknown))
                )

                response.campaigns == null -> {
                    emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
                }

                else -> {
                    emit(Resource.Success(response.campaigns.map { it.toCampaign() }))
                }
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<BrowseCampaignDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<BrowseCampaignDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun getCampaignDetail(id: Int): Flow<Resource<CampaignDetail>> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = discoverApi.getCampaignDetail(bearerToken = bearerToken, id = id)

            when (response.error) {
                true -> emit(Resource.Error(
                    uiText = response.message?.let { UIText.DynamicString(it) }
                        ?: UIText.StringResource(R.string.em_unknown))
                )
                else -> emit(Resource.Success(response.toCampaignDetails()))
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<CampaignDetailDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<CampaignDetailDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun getCategories(): Flow<Resource<List<Category>>> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = discoverApi.getCategories(bearerToken = bearerToken)

            when {
                response.error == true -> emit(Resource.Error(
                    uiText = response.message?.let { UIText.DynamicString(it) }
                        ?: UIText.StringResource(R.string.em_unknown))
                )

                response.categories == null -> {
                    emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
                }

                else -> {
                    emit(Resource.Success(response.categories.map { it.toCategories() }))
                }
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<CategoriesDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<CategoriesDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun getDashboard(): Flow<Resource<Dashboard>> = flow {
        emit(Resource.Loading())

        try {
            val idToken = authApi.getIdToken(true)
            val bearerToken = "Bearer $idToken"
            val response = discoverApi.getDashboard(bearerToken = bearerToken)

            when {
                response.error == true -> emit(Resource.Error(
                    uiText = response.message?.let { UIText.DynamicString(it) }
                        ?: UIText.StringResource(R.string.em_unknown))
                )

                response.tasks == null ||
                        response.completedCampaigns == null -> {
                    emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
                }

                else -> emit(
                    Resource.Success(
                        Dashboard(
                            tasks = response.tasks.map { it.toDashboardTask() },
                            completedCampaigns = response.completedCampaigns.map { it.toCampaign() }
                        )
                    )
                )
            }

        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> {
                    try {
                        val response = Gson().fromJson<DashboardDto>(
                            e.response()?.errorBody()?.charStream(),
                            object : TypeToken<DashboardDto>() {}.type
                        )
                        UIText.DynamicString(response.message!!)
                    } catch (e: Exception) {
                        UIText.StringResource(R.string.em_unknown)
                    }
                }

                is IOException -> UIText.StringResource(R.string.em_io_exception)

                else -> UIText.StringResource(R.string.em_unknown)

            }.let { emit(Resource.Error(it)) }
        }
    }

    override fun setCompletionProof(
        photo: File?,
        caption: String?,
        taskId: Int?
    ): Flow<SimpleResource> = flow {
        TODO("Not yet implemented")
    }

    override fun setJoinCampaign(campaignId: Int?): Flow<SimpleResource> = flow {
        TODO("Not yet implemented")
    }

    override fun setCompleteCampaign(campaignId: Int?): Flow<SimpleResource> = flow {
        TODO("Not yet implemented")
    }
}