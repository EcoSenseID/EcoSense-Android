package com.ecosense.android.featForums.data.model

import com.ecosense.android.featForums.domain.model.Supporter
import com.google.gson.annotations.SerializedName

data class SupporterDto(
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("id") val id: Int?,
    @SerializedName("userId") val userId: Int?,
    @SerializedName("name") val name: String?,
) {
    fun toDomain(): Supporter = Supporter(
        id = this.id ?: 0,
        userId = this.userId ?: 0,
        avatarUrl = this.avatarUrl ?: "",
        name = this.name ?: "",
    )
}