package com.ecosense.android.featForums.presentation.forums.model

data class SupportersDialogState(
    val isVisible: Boolean,
    val storyId: Int?,
) {
    companion object {
        val defaultValue = SupportersDialogState(
            isVisible = false,
            storyId = null,
        )
    }
}