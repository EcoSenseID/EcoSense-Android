package com.ecosense.android.featNotifications.data.api

import com.ecosense.android.featNotifications.data.model.GetNotificationsDto
import com.ecosense.android.featNotifications.data.model.NotificationDto
import kotlinx.coroutines.delay

class FakeNotificationsApi : NotificationsApi {
    override suspend fun getNotifications(
        bearerToken: String, language: String
    ): GetNotificationsDto {
        delay(500L)
        return GetNotificationsDto(error = false,
            message = "success",
            notifications = (1..100).map {
                NotificationDto(
                    content = "Lorem ipsum $it",
                    deeplink = if (it % 2 == 0) "https://ecosense.id/deeplinks/storydetail/$it" else null,
                    iconUrl = "https://i.pravatar.cc/300?img=$it",
                    id = it,
                    timestamp = (System.currentTimeMillis()/1000) - (it * 40000),
                )
            },
        )
    }
}