package com.ecosense.android.di

import android.content.Context
import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.featReward.data.api.RewardApi
import com.ecosense.android.featReward.data.repository.RewardRepositoryImpl
import com.ecosense.android.featReward.domain.repository.RewardRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RewardModule {
    @Provides
    @Singleton
    fun provideRewardApi(
        coreRetrofit: Retrofit
    ): RewardApi {
        return coreRetrofit.create(RewardApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRewardRepository(
        authApi: AuthApi,
        rewardApi: RewardApi,
        @ApplicationContext appContext: Context
    ): RewardRepository {
        return RewardRepositoryImpl(
            authApi = authApi,
            rewardApi = rewardApi,
            appContext = appContext
        )
    }
}