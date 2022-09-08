package com.ecosense.android.featForums.presentation

import com.ecosense.android.core.util.UIText
import com.ecosense.android.featForums.domain.model.Story

data class StoriesState(
    val isLoading: Boolean,
    val stories: List<Story>,
    val error: UIText?,
    val endReached: Boolean,
    val page: Int,
) {
    companion object {
        val defaultValue = StoriesState(
            isLoading = false,
            stories = emptyList(),
            error = null,
            endReached = false,
            page = 0,
        )
    }
}