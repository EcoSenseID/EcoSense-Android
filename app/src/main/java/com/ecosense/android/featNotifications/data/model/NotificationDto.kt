package com.ecosense.android.featNotifications.data.model

import com.ecosense.android.featNotifications.domain.model.Notification

data class NotificationDto(
    val content: String?,
    val deeplink: String?,
    val iconUrl: String?,
    val id: Int?,
    val timestamp: Long?,
) {
    fun toDomain() = Notification(
        content = this.content ?: "",
        deeplink = this.deeplink,
        iconUrl = this.iconUrl ?: "",
        id = this.id ?: 0,
        timestamp = (this.timestamp ?: 0) * 1000,
    )
}