package com.ecosense.android.di

import com.ecosense.android.core.domain.repository.AuthRepository
import com.ecosense.android.featAuth.domain.usecase.GoogleSignInUseCase
import com.ecosense.android.featAuth.domain.usecase.LoginUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideLoginUseCases(
        authRepository: AuthRepository
    ): LoginUseCases = LoginUseCases(
        googleSignInUseCase = GoogleSignInUseCase(authRepository = authRepository)
    )
}