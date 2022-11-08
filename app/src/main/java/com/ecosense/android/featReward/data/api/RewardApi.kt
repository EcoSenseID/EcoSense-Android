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
        @Query("categoryId") categoryId: Int
    ): RewardsDto

    @GET("myrewards")
    suspend fun getMyRewards(
        @Header("Authorization") bearerToken: String
    ): MyRewardsDto

    @GET("rewarddetail")
    suspend fun getRewardDetail(
        @Header("Authorization") bearerToken: String,
        @Query("rewardId") rewardId: Int
    ): RewardDetailDto

    @GET("myrewarddetail")
    suspend fun getMyRewardDetail(
        @Header("Authorization") bearerToken: String,
        @Query("claimId") claimId: Int
    ): MyRewardDetailDto

    @FormUrlEncoded
    @POST("redeemreward")
    suspend fun setRedeemReward(
        @Header("Authorization") bearerToken: String,
        @Field("rewardId") rewardId: Int
    ): RedeemRewardDto

    @FormUrlEncoded
    @POST("requestreward")
    suspend fun setRequestReward(
        @Header("Authorization") bearerToken: String,
        @Field("rewardId") rewardId: Int,
        @Field("email") email: String,
        @Field("walletType") walletType: String,
        @Field("walletNumber") walletNumber: String
    ): RequestRewardDto

    @FormUrlEncoded
    @POST("usereward")
    suspend fun setUseReward(
        @Header("Authorization") bearerToken: String,
        @Field("claimId") claimId: Int
    ): UseRewardDto
}