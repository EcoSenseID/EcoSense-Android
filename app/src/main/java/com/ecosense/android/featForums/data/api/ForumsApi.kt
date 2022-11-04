package com.ecosense.android.featForums.data.api

import com.ecosense.android.featForums.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
        @Part("caption") caption: RequestBody,
        @Part("sharedCampaignId") sharedCampaignId: RequestBody? = null,
        @Part attachedPhoto: MultipartBody.Part? = null,
    ): PostNewStoryDto

    @Multipart
    @POST("postreply")
    suspend fun postNewReply(
        @Header("Authorization") bearerToken: String,
        @Part("storyId") storyId: RequestBody,
        @Part("caption") caption: RequestBody,
        @Part attachedPhoto: MultipartBody.Part? = null,
    ): PostNewReplyDto

    @FormUrlEncoded
    @POST("supportstory")
    suspend fun postSupportStory(
        @Header("Authorization") bearerToken: String,
        @Field("storyId") storyId: Int,
    ): PostSupportStoryDto

    @FormUrlEncoded
    @POST("unsupportstory")
    suspend fun postUnsupportStory(
        @Header("Authorization") bearerToken: String,
        @Field("storyId") storyId: Int,
    ): PostUnsupportStoryDto

    @FormUrlEncoded
    @POST("supportreply")
    suspend fun postSupportReply(
        @Header("Authorization") bearerToken: String,
        @Field("replyId") replyId: Int,
    ): PostSupportReplyDto

    @FormUrlEncoded
    @POST("unsupportreply")
    suspend fun postUnsupportReply(
        @Header("Authorization") bearerToken: String,
        @Field("replyId") replyId: Int,
    ): PostUnsupportReplyDto
}