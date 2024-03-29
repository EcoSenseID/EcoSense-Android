package com.ecosense.android.featForums.presentation.forums.model

import com.ecosense.android.core.util.UIText

data class StoriesFeedState(
    val isLoading: Boolean,
    val isEndReached: Boolean,
    val errorMessage: UIText?,
    val page: Int,
) {
    companion object {
        val defaultValue = StoriesFeedState(
            isLoading = false,
            isEndReached = false,
            errorMessage = null,
            page = 1,
        )
    }
}