package com.ecosense.android.di

import android.content.Context
import com.ecosense.android.core.domain.api.AuthApi
import com.ecosense.android.featForums.data.api.FakeForumsApi
import com.ecosense.android.featForums.data.api.ForumsApi
import com.ecosense.android.featForums.data.repository.ForumsRepositoryImpl
import com.ecosense.android.featForums.domain.repository.ForumsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ForumsModule {

    @Provides
    @Singleton
    fun provideRepository(
        authApi: AuthApi, forumsApi: ForumsApi, @ApplicationContext context: Context
    ): ForumsRepository = ForumsRepositoryImpl(
        authApi = authApi,
        forumsApi = forumsApi,
        appContext = context,
    )

    @Provides
    @Singleton
    fun provideApi(
        coreRetrofit: Retrofit
    ): ForumsApi {
//        return FakeForumsApi()
        return coreRetrofit.create(ForumsApi::class.java)
    }
}