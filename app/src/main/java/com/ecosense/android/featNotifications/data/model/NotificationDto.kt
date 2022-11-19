package com.ecosense.android.featNotifications.data.model

import com.ecosense.android.featNotifications.domain.model.Notification
import com.google.gson.annotations.SerializedName

data class NotificationDto(
    @SerializedName("content") val content: String?,
    @SerializedName("deeplink") val deeplink: String?,
    @SerializedName("iconUrl") val iconUrl: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("timestamp") val timestamp: Long?,
) {
    fun toDomain() = Notification(
        content = this.content ?: "",
        deeplink = this.deeplink,
        iconUrl = this.iconUrl ?: "",
        id = this.id ?: 0,
        timestamp = (this.timestamp ?: 0) * 1000,
    )
}