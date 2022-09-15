package com.ecosense.android.featReward.data.repository

import android.content.Context
import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.featDiscoverCampaign.data.api.DiscoverApi
import com.ecosense.android.featReward.data.api.RewardApi
import com.ecosense.android.featReward.domain.repository.RewardRepository

class RewardRepositoryImpl(
    private val authApi: AuthApi,
    private val rewardApi: RewardApi,
    private val appContext: Context
) : RewardRepository {
}