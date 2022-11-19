package com.ecosense.android.featForums.data.model

import com.google.gson.annotations.SerializedName

data class PostSupportStoryDto(
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
)