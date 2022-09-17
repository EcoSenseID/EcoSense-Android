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

    @GET("comments")
    suspend fun getStoryComments(
        @Header("Authorization") bearerToken: String,
        @Query("storyId") storyId: Int,
    ): GetCommentsDto

    @GET("likes")
    suspend fun getStoryLikes(
        @Header("Authorization") bearerToken: String,
        @Query("storyId") storyId: Int,
    ): GetStoryLikesDto

    @Multipart
    @POST("poststory")
    suspend fun postNewStory(
        @Header("Authorization") bearerToken: String,
        @Part campaignId: Int? = null,
        @Part caption: String,
        @Part photo: MultipartBody.Part?,
    ): PostNewStoryDto

    @Multipart
    @POST("postcomment")
    suspend fun postNewComment(
        @Header("Authorization") bearerToken: String,
        @Part storyId: Int,
        @Part content: String,
        @Part photo: MultipartBody.Part?,
    ): PostNewCommentDto

    @FormUrlEncoded
    @POST("likestory")
    suspend fun postLikeStory(
        @Header("Authorization") bearerToken: String,
        @Field("storyId") storyId: Int,
    ): PostLikeStoryDto

    @FormUrlEncoded
    @POST("likecomment")
    suspend fun postLikeComment(
        @Header("Authorization") bearerToken: String,
        @Field("commentId") commentId: Int,
    ): PostLikeCommentDto
}