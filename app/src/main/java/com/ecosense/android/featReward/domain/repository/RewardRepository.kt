package com.ecosense.android.featReward.domain.repository

import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.featReward.domain.model.*
import kotlinx.coroutines.flow.Flow

interface RewardRepository {
    fun getRewardHomepage(): Flow<Resource<RewardHomepage>>

    fun getRewards(categoryId: Int): Flow<Resource<List<Rewards>>>

    fun getMyRewards(): Flow<Resource<List<MyRewards>>>

    fun getRewardDetail(rewardId: Int): Flow<Resource<RewardDetail>>

    fun getMyRewardDetail(claimId: Int): Flow<Resource<MyRewardDetail>>

    fun setRedeemReward(
        rewardId: Int,
        rewardCategory: String
    ): Flow<SimpleResource>

    fun setRequestReward(
        rewardId: Int,
        email: String,
        walletType: String,
        walletNumber: String
    ): Flow<SimpleResource>

    fun setUseReward(
        claimId: Int
    ): Flow<SimpleResource>
}