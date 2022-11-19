package com.ecosense.android.featNotifications.data.model

import com.google.gson.annotations.SerializedName

data class GetNotificationsDto(
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("notifications") val notifications: List<NotificationDto>?,
)