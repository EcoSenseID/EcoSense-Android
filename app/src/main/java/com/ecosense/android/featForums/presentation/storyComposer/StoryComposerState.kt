package com.ecosense.android.featForums.presentation.storyComposer

data class StoryComposerState(
    val avatarUrl: String?,
    val caption: String?,
    val attachedPhotoUrl: String?,
    val isUploading: Boolean,

    ) {
    companion object {
        val defaultValue = StoryComposerState(
            avatarUrl = null,
            caption = null,
            attachedPhotoUrl = null,
            isUploading = false,
        )
    }
}