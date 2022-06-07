package com.ecosense.android.di

import android.content.Context
import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.featRecognition.data.remote.RecognitionApi
import com.ecosense.android.featRecognition.data.repository.RecognitionRepositoryImpl
import com.ecosense.android.featRecognition.data.source.DiseaseDataSource
import com.ecosense.android.featRecognition.domain.repository.RecognitionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecognitionModule {
    @Provides
    @Singleton
    fun provideDiseaseDataSource(@ApplicationContext appContext: Context): DiseaseDataSource {
        return DiseaseDataSource(appContext)
    }

    @Provides
    @Singleton
    fun provideRecognitionApi(
        coreRetrofit: Retrofit
    ): RecognitionApi {
        return coreRetrofit.create(RecognitionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDiseaseRecognitionRepository(
        @ApplicationContext appContext: Context,
        recognitionApi: RecognitionApi,
        diseaseDataSource: DiseaseDataSource,
        authApi: AuthApi,
    ): RecognitionRepository {
        return RecognitionRepositoryImpl(
            appContext = appContext,
            diseaseDataSource = diseaseDataSource,
            recognitionApi = recognitionApi,
            authApi = authApi,
        )
    }
}