package com.ecosense.android.core.data.model

import com.ecosense.android.core.domain.model.Category
import com.google.gson.annotations.SerializedName

data class CategoryDto(
    @SerializedName("name") val name: String?,
    @SerializedName("colorHex") val colorHex: String?,
) {
    fun toDomain(): Category = Category(
        name = this.name ?: "",
        colorHex = this.colorHex ?: "",
    )
}