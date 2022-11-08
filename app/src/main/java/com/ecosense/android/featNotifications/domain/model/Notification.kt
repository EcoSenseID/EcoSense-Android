package com.ecosense.android.featNotifications.domain.model

data class Notification(
    val content: String,
    val deeplink: String?,
    val iconUrl: String,
    val id: Int,
    val timestamp: Long,
)