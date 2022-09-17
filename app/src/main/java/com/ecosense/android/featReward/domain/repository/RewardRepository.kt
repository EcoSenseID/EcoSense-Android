package com.ecosense.android.featReward.domain.repository

import com.ecosense.android.core.util.Resource
import com.ecosense.android.core.util.SimpleResource
import com.ecosense.android.featReward.domain.model.MyRewards
import com.ecosense.android.featReward.domain.model.RewardDetail
import com.ecosense.android.featReward.domain.model.RewardHomepage
import com.ecosense.android.featReward.domain.model.Rewards
import kotlinx.coroutines.flow.Flow

interface RewardRepository {
    fun getRewardHomepage(): Flow<Resource<RewardHomepage>>

    fun getRewards(rewardCategory: String): Flow<Resource<Rewards>>

    fun getMyRewards(): Flow<Resource<List<MyRewards>>>

    fun getRewardDetail(rewardId: Int): Flow<Resource<RewardDetail>>

    fun setRedeemReward(
        rewardId: Int
    ): Flow<SimpleResource>

    fun setUseReward(
        rewardId: Int
    ): Flow<SimpleResource>
}