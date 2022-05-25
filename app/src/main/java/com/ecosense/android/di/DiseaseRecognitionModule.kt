package com.ecosense.android.di

import android.content.Context
import com.ecosense.android.featDiseaseRecognition.data.repository.DiseaseRecognitionRepositoryImpl
import com.ecosense.android.featDiseaseRecognition.domain.repository.DiseaseRecognitionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiseaseRecognitionModule {
    @Provides
    @Singleton
    fun provideDiseaseRecognitionRepository(
        @ApplicationContext appContext: Context
    ): DiseaseRecognitionRepository {
        return DiseaseRecognitionRepositoryImpl(
            appContext = appContext
        )
    }
}