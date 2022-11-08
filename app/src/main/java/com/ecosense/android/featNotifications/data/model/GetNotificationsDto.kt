package com.ecosense.android.featNotifications.data.model

data class GetNotificationsDto(
    val error: Boolean?,
    val message: String?,
    val notifications: List<NotificationDto>?,
)