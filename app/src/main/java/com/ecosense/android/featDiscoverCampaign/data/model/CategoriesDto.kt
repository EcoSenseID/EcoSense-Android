package com.ecosense.android.featDiscoverCampaign.data.model

import com.ecosense.android.featDiscoverCampaign.domain.model.Category
import com.google.gson.annotations.SerializedName

data class CategoriesDto(
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("categories") val categories: List<CategoriesItem>?
)

data class CategoriesItem(
    @SerializedName("id") val id: Int?,
    @SerializedName("photoUrl") val photoUrl: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("colorHex") val colorHex: String?
) {
    fun toCategories() = Category(
        id = id ?: 0,
        photoUrl = photoUrl ?: "",
        name = name ?: "",
        colorHex = colorHex ?: "",
    )
}

