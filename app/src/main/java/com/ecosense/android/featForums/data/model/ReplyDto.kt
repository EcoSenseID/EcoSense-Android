package com.ecosense.android.featForums.data.model

import com.ecosense.android.featForums.domain.model.Reply
import com.google.gson.annotations.SerializedName

data class ReplyDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("userId") val userId: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("caption") val caption: String?,
    @SerializedName("attachedPhotoUrl") val attachedPhotoUrl: String?,
    @SerializedName("createdAt") val createdAt: Long?,
    @SerializedName("supportersCount") val supportersCount: Int?,
    @SerializedName("isSupported") val isSupported: Boolean?,
) {
    fun toDomain(): Reply = Reply(
        id = this.id ?: 0,
        userId = this.userId ?: 0,
        name = this.name ?: "",
        avatarUrl = this.avatarUrl ?: "",
        caption = this.caption ?: "",
        attachedPhotoUrl = this.attachedPhotoUrl,
        createdAt = (this.createdAt ?: 0) * 1000,
        supportersCount = this.supportersCount ?: 0,
        isSupported = this.isSupported ?: false,
    )
}