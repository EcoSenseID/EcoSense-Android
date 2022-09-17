package com.ecosense.android.featForums.presentation.forums.model

import com.ecosense.android.core.util.UIText
import com.ecosense.android.featForums.presentation.model.StoryPresentation

data class StoriesFeedState(
    val stories: List<StoryPresentation>,
    val isLoading: Boolean,
    val isEndReached: Boolean,
    val errorMessage: UIText?,
    val page: Int,
) {
    companion object {
        val defaultValue = StoriesFeedState(
            stories = emptyList(),
            isLoading = false,
            isEndReached = false,
            errorMessage = null,
            page = 0,
        )
    }
}