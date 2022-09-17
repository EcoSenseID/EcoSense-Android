package com.ecosense.android.featForums.presentation.storyDetail.model

import com.ecosense.android.core.util.UIText

data class CommentsFeedState(
    val comments: List<CommentPresentation>,
    val isLoading: Boolean,
    val isEndReached: Boolean,
    val errorMessage: UIText?,
    val page: Int,
) {
    companion object {
        val defaultValue = CommentsFeedState(
            comments = emptyList(),
            isLoading = false,
            isEndReached = false,
            errorMessage = null,
            page = 0,
        )
    }
}