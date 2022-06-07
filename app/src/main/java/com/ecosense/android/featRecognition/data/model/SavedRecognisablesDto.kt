package com.ecosense.android.featRecognition.data.model

data class SavedRecognisablesDto(
    val error: Boolean?,
    val message: String?,
    val recognisables: List<RecognisableDto>?,
)