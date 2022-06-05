package com.ecosense.android.featRecognition.presentation.saved

import com.ecosense.android.featRecognition.domain.model.SavedRecognisable

data class SavedRecognisablesScreenState(
    val savedRecognisables: List<SavedRecognisable>,
    val isLoading: Boolean,
) {
    companion object {
        val defaultValue = SavedRecognisablesScreenState(
            savedRecognisables = emptyList(),
            isLoading = false
        )
    }
}