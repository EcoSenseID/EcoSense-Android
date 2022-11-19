package com.ecosense.android.featRecognition.data.model

import com.google.gson.annotations.SerializedName

data class SaveRecognisableDto(
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("recognisableId") val recognisableId: Int?
)