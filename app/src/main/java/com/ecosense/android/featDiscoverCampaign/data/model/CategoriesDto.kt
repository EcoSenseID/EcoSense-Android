package com.ecosense.android.featDiscoverCampaign.data.model

data class CategoriesDto(
    val error: Boolean? = null,
    val message: String? = null,
    val categories: List<CategoriesItem?>? = null
)

data class CategoriesItem(
    val id: Int? = null,
    val photoUrl: String? = null,
    val name: String? = null
)

