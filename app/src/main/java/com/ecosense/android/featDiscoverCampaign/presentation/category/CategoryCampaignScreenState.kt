package com.ecosense.android.featDiscoverCampaign.presentation.category

import com.ecosense.android.featDiscoverCampaign.domain.model.Category

data class CategoryCampaignScreenState(
    val categories: List<Category>,
    val isLoadingCategories: Boolean
) {
    companion object {
        val defaultValue = CategoryCampaignScreenState(
            categories = emptyList(),
            isLoadingCategories = false
        )
    }
}