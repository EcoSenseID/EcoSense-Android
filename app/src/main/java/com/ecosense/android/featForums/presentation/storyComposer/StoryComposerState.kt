package com.ecosense.android.featForums.presentation.storyComposer

import android.net.Uri
import com.ecosense.android.core.presentation.model.SharedCampaignPresentation

data class StoryComposerState(
    val avatarUrl: String?,
    val caption: String,
    val attachedPhotoUri: Uri?,
    val sharedCampaign: SharedCampaignPresentation?,
    val isUploading: Boolean,
) {
    companion object {
        val defaultValue = StoryComposerState(
            avatarUrl = null,
            caption = "",
            attachedPhotoUri = null,
            sharedCampaign = null,
            isUploading = false,
        )
    }
}