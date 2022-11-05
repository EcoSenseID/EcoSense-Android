package com.ecosense.android.featProfile.data.api

import com.ecosense.android.featProfile.data.model.GetCampaignsHistoryDto
import com.ecosense.android.featProfile.data.model.GetStoriesHistoryDto
import com.ecosense.android.featProfile.data.model.ProfileDto
import retrofit2.http.GET
import retrofit2.http.Header

interface ProfileApi {
    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") bearerToken: String,
    ): ProfileDto

    @GET("storieshistory")
    suspend fun getStoriesHistory(
        @Header("Authorization") bearerToken: String,
    ): GetStoriesHistoryDto

    @GET("campaignshistory")
    suspend fun getCampaignsHistory(
        @Header("Authorization") bearerToken: String,
    ): GetCampaignsHistoryDto
}