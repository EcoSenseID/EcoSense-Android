package com.ecosense.android.featRecognition.data.model

import com.google.gson.annotations.SerializedName

data class RecognisableDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("label") val label: String?,
    @SerializedName("confidencePercent") val confidencePercent: Int?,
    @SerializedName("savedAt") val savedAt: Int?
)