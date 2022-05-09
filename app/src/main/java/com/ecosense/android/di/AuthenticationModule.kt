package com.ecosense.android.di

import com.ecosense.android.featAuthentication.data.repository.AuthenticationRepositoryImpl
import com.ecosense.android.featAuthentication.domain.repository.AuthenticationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    @Provides
    @Singleton
    fun provideRepository(): AuthenticationRepository =
        AuthenticationRepositoryImpl()
}