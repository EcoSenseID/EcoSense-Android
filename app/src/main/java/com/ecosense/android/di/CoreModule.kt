package com.ecosense.android.di

import android.content.Context
import com.ecosense.android.core.data.api.FirebaseAuthApi
import com.ecosense.android.core.data.api.FirebaseStorageApi
import com.ecosense.android.core.data.repository.AuthRepositoryImpl
import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.core.domain.api.CloudStorageApi
import com.ecosense.android.core.domain.repository.AuthRepository
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideAuthApi(
        @ApplicationContext appContext: Context,
    ): AuthApi {
        val firebaseAuth = try {
            FirebaseAuth.getInstance().apply { useAppLanguage() }
        } catch (e: Exception) {
            FirebaseApp.initializeApp(appContext)
            FirebaseAuth.getInstance().apply { useAppLanguage() }
        }

        return FirebaseAuthApi(
            firebaseAuth = firebaseAuth,
        )
    }

    @Provides
    @Singleton
    fun provideCloudStorageApi(): CloudStorageApi = FirebaseStorageApi()

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi
    ): AuthRepository = AuthRepositoryImpl(authApi = authApi)

    @Provides
    @Singleton
    fun provideCoreRetrofit(): Retrofit {
        val interceptedClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        return Retrofit.Builder().baseUrl("https://ecosense-bangkit.uc.r.appspot.com/")
//            .baseUrl("https://devapi-dot-ecosense-bangkit.uc.r.appspot.com/")
//            .apply { if (BuildConfig.DEBUG) client(interceptedClient) }
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}