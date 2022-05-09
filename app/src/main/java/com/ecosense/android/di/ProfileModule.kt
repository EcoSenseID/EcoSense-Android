package com.ecosense.android.di

import com.ecosense.android.featProfile.data.repository.ProfileRepositoryImpl
import com.ecosense.android.featProfile.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {
    @Provides
    @Singleton
    fun provideRepository(): ProfileRepository =
        ProfileRepositoryImpl()
}