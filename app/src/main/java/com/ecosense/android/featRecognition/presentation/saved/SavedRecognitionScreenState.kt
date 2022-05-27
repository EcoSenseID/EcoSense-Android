package com.ecosense.android.featRecognition.presentation.saved

import com.ecosense.android.featRecognition.domain.model.SavedRecognitionResult

data class SavedRecognitionScreenState(
    val resultList: List<SavedRecognitionResult>,
    val isLoading: Boolean,
) {
    companion object {
        val defaultValue = SavedRecognitionScreenState(
            resultList = emptyList(),
            isLoading = false
        )
    }
}