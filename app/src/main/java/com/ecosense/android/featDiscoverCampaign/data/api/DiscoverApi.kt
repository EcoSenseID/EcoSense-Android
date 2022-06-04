package com.ecosense.android.featDiscoverCampaign.data.api

import com.ecosense.android.featDiscoverCampaign.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface DiscoverApi {
    @GET("campaign")
    suspend fun getCampaigns(
        @Header("Authorization") bearerToken: String,
        @Query("q") q: String?,
        @Query("categoryId") categoryId: Int?
    ): BrowseCampaignDto

    @GET("dashboard")
    suspend fun getDashboard(
        @Header("Authorization") bearerToken: String
    ): DashboardDto

    @GET("categories")
    suspend fun getCategories(
        @Header("Authorization") bearerToken: String
    ): CategoriesDto

    @GET("detail")
    suspend fun getCampaignDetail(
        @Header("Authorization") bearerToken: String,
        @Query("id") id: Int
    ): CampaignDetailDto

    @Multipart
    @POST("proof")
    suspend fun setCompletionProof(
        @Header("Authorization") bearerToken: String,
        @Part("photo") photo: MultipartBody.Part,
        @Part("caption") caption: RequestBody,
        @Part("taskId") taskId: Int
    ): CompletionProofDto


    @POST("joincampaign")
    suspend fun setJoinCampaign(
        @Header("Authorization") bearerToken: String,
        @Field("campaignId") campaignId: Int
    ): JoinCampaignDto

    @POST("completecampaign")
    suspend fun setCompleteCampaign(
        @Header("Authorization") bearerToken: String,
        @Field("campaignId") campaignId: Int
    ): CompleteCampaignDto
}