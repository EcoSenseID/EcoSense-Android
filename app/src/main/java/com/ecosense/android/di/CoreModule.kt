package com.ecosense.android.di

import com.ecosense.android.core.data.repository.AuthenticationRepositoryImpl
import com.ecosense.android.core.domain.repository.AuthenticationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    @Provides
    @Singleton
    fun provideAuthenticationRepository(): AuthenticationRepository =
        AuthenticationRepositoryImpl()
}