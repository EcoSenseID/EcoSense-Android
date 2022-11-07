package com.ecosense.android.featNotifications.data.api

import com.ecosense.android.featNotifications.data.model.GetNotificationsDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NotificationsApi {

    @GET("notifications")
    suspend fun getNotifications(
        @Header("Authorization") bearerToken: String,
        @Query("language") language: String,
    ): GetNotificationsDto
    
}