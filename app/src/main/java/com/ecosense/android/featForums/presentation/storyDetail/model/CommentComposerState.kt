package com.ecosense.android.featForums.presentation.storyDetail.model

data class CommentComposerState(
    val profilePictureUrl: String?,
    val caption: String?,
    val attachedPhotoUrl: String?,
    val isUploading: Boolean,
) {
    companion object {
        val defaultValue = CommentComposerState(
            profilePictureUrl = null,
            caption = null,
            attachedPhotoUrl = null,
            isUploading = false,
        )
    }
}