package com.ecosense.android.featNotifications.presentation.model

import com.ecosense.android.featNotifications.domain.model.Notification

data class NotificationPresentation(
    val content: String,
    val deeplink: String?,
    val iconUrl: String,
    val id: Int,
    val timestamp: Long,
)

fun Notification.toPresentation() = NotificationPresentation(
    content = this.content,
    deeplink = this.deeplink,
    iconUrl = this.iconUrl,
    id = this.id,
    timestamp = this.timestamp,
)