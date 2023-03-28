package com.ecosense.android.featForums.presentation.storyDetail.model

import android.net.Uri

data class ReplyComposerState(
    val avatarUrl: String?,
    val caption: String,
    val attachedPhotoUri: Uri?,
    val isUploading: Boolean,
    val isFocused: Boolean,
) {
    companion object {
        val defaultValue = ReplyComposerState(
            avatarUrl = null,
            caption = "",
            attachedPhotoUri = null,
            isUploading = false,
            isFocused = false,
        )
    }
}