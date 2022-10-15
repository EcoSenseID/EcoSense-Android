package com.ecosense.android.featForums.data.api

import com.ecosense.android.featForums.data.model.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface ForumsApi {

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") bearerToken: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): GetStoriesDto

    @GET("replies")
    suspend fun getStoryReplies(
        @Header("Authorization") bearerToken: String,
        @Query("storyId") storyId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): GetRepliesDto

    @GET("supporters")
    suspend fun getStorySupporters(
        @Header("Authorization") bearerToken: String,
        @Query("storyId") storyId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): GetStorySupportersDto

    @Multipart
    @POST("poststory")
    suspend fun postNewStory(
        @Header("Authorization") bearerToken: String,
        @Part sharedCampaignId: Int? = null,
        @Part caption: String,
        @Part attachedPhoto: MultipartBody.Part?,
    ): PostNewStoryDto

    @Multipart
    @POST("postreply")
    suspend fun postNewReply(
        @Header("Authorization") bearerToken: String,
        @Part storyId: Int,
        @Part caption: String,
        @Part attachedPhoto: MultipartBody.Part?,
    ): PostNewReplyDto

    @FormUrlEncoded
    @POST("supportstory")
    suspend fun postSupportStory(
        @Header("Authorization") bearerToken: String,
        @Field("storyId") storyId: Int,
    ): PostSupportStoryDto

    @FormUrlEncoded
    @POST("supportreply")
    suspend fun postSupportReply(
        @Header("Authorization") bearerToken: String,
        @Field("replyId") replyId: Int,
    ): PostSupportReplyDto
}