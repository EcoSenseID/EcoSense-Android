package com.ecosense.android.featForums.presentation.storyDetail.model

data class ReplyComposerState(
    val avatarUrl: String?,
    val caption: String?,
    val attachedPhotoUrl: String?,
    val isUploading: Boolean,
) {
    companion object {
        val defaultValue = ReplyComposerState(
            avatarUrl = null,
            caption = null,
            attachedPhotoUrl = null,
            isUploading = false,
        )
    }
}