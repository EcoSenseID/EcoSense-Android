package com.ecosense.android.di

import com.ecosense.android.featDiscoverCampaign.data.repository.DiscoverCampaignRepositoryImpl
import com.ecosense.android.featDiscoverCampaign.domain.repository.DiscoverCampaignRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiscoverCampaignModule {

    @Provides
    @Singleton
    fun provideRepository(): DiscoverCampaignRepository =
        DiscoverCampaignRepositoryImpl()
}