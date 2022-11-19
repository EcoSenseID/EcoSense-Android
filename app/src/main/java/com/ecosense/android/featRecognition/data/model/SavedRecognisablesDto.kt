package com.ecosense.android.featRecognition.data.model

import com.google.gson.annotations.SerializedName

data class SavedRecognisablesDto(
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("recognisables") val recognisables: List<RecognisableDto>?,
)