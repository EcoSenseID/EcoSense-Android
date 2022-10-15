package com.ecosense.android.featForums.data.model

import com.ecosense.android.featForums.domain.model.Supporter

data class SupporterDto(
    val avatarUrl: String?,
    val id: Int?,
    val name: String?,
) {
    fun toDomain(): Supporter = Supporter(
        id = this.id ?: 0,
        avatarUrl = this.avatarUrl ?: "",
        name = this.name ?: "",
    )
}