package com.ecosense.android.featDiscoverCampaign.data.model

import com.ecosense.android.featDiscoverCampaign.domain.model.Category

data class CategoriesDto(
    val error: Boolean?,
    val message: String?,
    val categories: List<CategoriesItem>?
)

data class CategoriesItem(
    val id: Int?,
    val photoUrl: String?,
    val name: String?
) {
    fun toCategories() = Category(
        id = id ?: 0,
        photoUrl = photoUrl ?: "",
        name = name ?: ""
    )
}

