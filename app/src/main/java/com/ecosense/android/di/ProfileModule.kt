package com.ecosense.android.di

import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.core.domain.api.CloudStorageApi
import com.ecosense.android.featProfile.data.api.ProfileApi
import com.ecosense.android.featProfile.data.repository.ProfileRepositoryImpl
import com.ecosense.android.featProfile.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {
    @Provides
    @Singleton
    fun provideProfileApi(
        coreRetrofit: Retrofit
    ): ProfileApi {
        return coreRetrofit.create(ProfileApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(
        authApi: AuthApi,
        profileApi: ProfileApi,
        cloudStorageApi: CloudStorageApi
    ): ProfileRepository {
        return ProfileRepositoryImpl(
            authApi = authApi,
            profileApi = profileApi,
            cloudStorageApi = cloudStorageApi
        )
    }
}