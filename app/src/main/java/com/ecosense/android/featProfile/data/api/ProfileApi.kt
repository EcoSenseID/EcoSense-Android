package com.ecosense.android.featProfile.data.api

import com.ecosense.android.featProfile.data.model.GetCampaignsHistoryDto
import com.ecosense.android.featProfile.data.model.GetStoriesHistoryDto
import com.ecosense.android.featProfile.data.model.OthersProfileDto
import com.ecosense.android.featProfile.data.model.ProfileDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ProfileApi {
    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") bearerToken: String,
    ): ProfileDto

    @GET("profile")
    suspend fun getOthersProfile(
        @Header("Authorization") bearerToken: String,
        @Query("userId") userId: Int,
    ): OthersProfileDto

    @GET("storieshistory")
    suspend fun getStoriesHistory(
        @Header("Authorization") bearerToken: String,
        @Query("userId") userId: Int?,
    ): GetStoriesHistoryDto

    @GET("campaignshistory")
    suspend fun getCampaignsHistory(
        @Header("Authorization") bearerToken: String,
        @Query("userId") userId: Int?,
    ): GetCampaignsHistoryDto
}