package com.ecosense.android.featReward.data.api

import com.ecosense.android.featReward.data.model.*
import retrofit2.http.*

interface RewardApi {
    @GET("rewardhome")
    suspend fun getRewardHomepage(
        @Header("Authorization") bearerToken: String
    ): RewardHomepageDto

    @GET("rewards")
    suspend fun getRewards(
        @Header("Authorization") bearerToken: String,
        @Query("rewardCategory") rewardCategory: String?
    ): RewardsDto

    @GET("myrewards")
    suspend fun getMyRewards(
        @Header("Authorization") bearerToken: String
    ): MyRewardsDto

    @GET("rewarddetail")
    suspend fun getRewardDetail(
        @Header("Authorization") bearerToken: String,
        @Query("rewardId") rewardId: Int?
    ): RewardDetailDto

    @FormUrlEncoded
    @POST("redeemreward")
    suspend fun setRedeemReward(
        @Header("Authorization") bearerToken: String,
        @Field("rewardId") rewardId: Int
    ): RedeemRewardDto

    @FormUrlEncoded
    @POST("usereward")
    suspend fun useRedeemReward(
        @Header("Authorization") bearerToken: String,
        @Field("rewardId") rewardId: Int
    ): UseRewardDto
}