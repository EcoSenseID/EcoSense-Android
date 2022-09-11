package com.ecosense.android.featForums.presentation.model

import com.ecosense.android.core.util.UIText

data class StoriesState(
    val isLoading: Boolean,
    val stories: List<StoryPresentation>,
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