package com.ecosense.android.featForums.presentation.storyDetail.model

import com.ecosense.android.core.util.UIText

data class RepliesFeedState(
    val replies: List<ReplyPresentation>,
    val isLoading: Boolean,
    val isEndReached: Boolean,
    val errorMessage: UIText?,
    val page: Int,
) {
    companion object {
        val defaultValue = RepliesFeedState(
            replies = emptyList(),
            isLoading = false,
            isEndReached = false,
            errorMessage = null,
            page = 0,
        )
    }
}