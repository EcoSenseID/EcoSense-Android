package com.ecosense.android.featForums.data.model

import com.ecosense.android.featForums.domain.model.Reply

data class ReplyDto(
    val id: Int?,
    val userId: Int?,
    val name: String?,
    val avatarUrl: String?,
    val caption: String?,
    val attachedPhotoUrl: String?,
    val createdAt: Long?,
    val supportersCount: Int?,
    val isSupported: Boolean?,
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