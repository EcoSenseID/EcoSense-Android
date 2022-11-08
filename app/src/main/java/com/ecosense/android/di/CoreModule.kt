package com.ecosense.android.di

import com.ecosense.android.BuildConfig
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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
        val interceptedClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        return Retrofit.Builder()
//            .baseUrl("https://ecosense-bangkit.uc.r.appspot.com/") TODO: use prod base url
            .baseUrl("https://devapi-dot-ecosense-bangkit.uc.r.appspot.com/")
            .apply { if (BuildConfig.DEBUG) client(interceptedClient) }
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}