package com.ecosense.android.featReward.data.api

import com.ecosense.android.featReward.data.model.MyRewardDetailDto
import com.ecosense.android.featReward.data.model.MyRewardsDto
import com.ecosense.android.featReward.data.model.RewardHomepageDto
import com.ecosense.android.featReward.data.model.RewardsDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

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
        @Header("Authorization") bearerToken: String
    ): MyRewardDetailDto
}