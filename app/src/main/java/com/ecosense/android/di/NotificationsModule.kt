package com.ecosense.android.di

import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.featNotifications.data.api.NotificationsApi
import com.ecosense.android.featNotifications.data.repository.NotificationsRepositoryImpl
import com.ecosense.android.featNotifications.domain.repository.NotificationsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationsModule {

    @Provides
    @Singleton
    fun provideRepository(
        authApi: AuthApi,
        notificationsApi: NotificationsApi,
    ): NotificationsRepository {
        return NotificationsRepositoryImpl(authApi = authApi, notificationsApi = notificationsApi)
    }

    @Provides
    @Singleton
    fun provideApi(
        coreRetrofit: Retrofit
    ): NotificationsApi {
        return coreRetrofit.create(NotificationsApi::class.java)
    }
}