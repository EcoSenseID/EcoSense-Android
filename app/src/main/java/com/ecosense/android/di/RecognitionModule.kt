package com.ecosense.android.di

import android.content.Context
import com.ecosense.android.core.data.local.EcoSenseDatabase
import com.ecosense.android.featRecognition.data.repository.RecognitionRepositoryImpl
import com.ecosense.android.featRecognition.data.source.DiseaseDataSource
import com.ecosense.android.featRecognition.domain.repository.RecognitionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideDiseaseRecognitionRepository(
        @ApplicationContext appContext: Context,
        database: EcoSenseDatabase,
        diseaseDataSource: DiseaseDataSource
    ): RecognitionRepository {
        return RecognitionRepositoryImpl(
            appContext = appContext,
            savedRecognisableDao = database.savedRecognisableDao,
            diseaseDataSource = diseaseDataSource,
        )
    }
}