package com.ecosense.android.core.data.model

import com.ecosense.android.core.domain.model.Category

data class CategoryDto(
    val name: String?,
    val colorHex: String?,
) {
    fun toDomain(): Category = Category(
        name = this.name ?: "",
        colorHex = this.colorHex ?: "",
    )
}