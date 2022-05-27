package com.ecosense.android.di

import android.app.Application
import androidx.room.Room
import com.ecosense.android.core.data.api.FirebaseAuthApi
import com.ecosense.android.core.data.api.FirebaseStorageApi
import com.ecosense.android.core.data.local.EcoSenseDatabase
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
            .baseUrl("https://api.jsonbin.io/b/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application): EcoSenseDatabase {
        return Room.databaseBuilder(
            app,
            EcoSenseDatabase::class.java,
            EcoSenseDatabase.NAME
        ).build()
    }
}