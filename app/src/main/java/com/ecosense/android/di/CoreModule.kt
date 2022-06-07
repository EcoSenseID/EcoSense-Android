package com.ecosense.android.di

import com.ecosense.android.core.data.api.FirebaseAuthApi
import com.ecosense.android.core.data.api.FirebaseStorageApi
import com.ecosense.android.core.data.repository.AuthRepositoryImpl
import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.core.domain.api.CloudStorageApi
import com.ecosense.android.core.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi = FirebaseAuthApi()

    @Provides
    @Singleton
    fun provideCloudStorageApi(): CloudStorageApi = FirebaseStorageApi()

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi
    ): AuthRepository =
        AuthRepositoryImpl(authApi = authApi)

    @Provides
    @Singleton
    fun provideCoreRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://ecosense-bangkit.uc.r.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}