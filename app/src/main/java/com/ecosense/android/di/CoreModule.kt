package com.ecosense.android.di

import com.ecosense.android.core.data.repository.AuthRepositoryImpl
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
    fun provideAuthRepository(): AuthRepository =
        AuthRepositoryImpl()

    @Provides
    @Singleton
    fun provideCoreRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.jsonbin.io/b/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}