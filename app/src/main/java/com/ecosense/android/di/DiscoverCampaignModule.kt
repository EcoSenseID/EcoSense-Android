package com.ecosense.android.di

import android.content.Context
import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.featDiscoverCampaign.data.api.DiscoverApi
import com.ecosense.android.featDiscoverCampaign.data.repository.DiscoverCampaignRepositoryImpl
import com.ecosense.android.featDiscoverCampaign.domain.repository.DiscoverCampaignRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiscoverCampaignModule {

    @Provides
    @Singleton
    fun provideDiscoverApi(
        coreRetrofit: Retrofit
    ): DiscoverApi {
        return coreRetrofit.create(DiscoverApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(
        authApi: AuthApi,
        discoverApi: DiscoverApi,
        @ApplicationContext appContext: Context
    ): DiscoverCampaignRepository {
        return DiscoverCampaignRepositoryImpl(
            authApi = authApi,
            discoverApi = discoverApi,
            appContext = appContext
        )
    }
}