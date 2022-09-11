package com.ecosense.android.featForums.presentation.model

data class StoryComposerState(
    val profilePictureUrl: String?,
    val caption: String?,
    val attachedPhotoUrl: String?,
    val isUploading: Boolean,
) {
    companion object {
        val defaultValue = StoryComposerState(
            profilePictureUrl = null,
            caption = null,
            attachedPhotoUrl = null,
            isUploading = false,
        )
    }
}