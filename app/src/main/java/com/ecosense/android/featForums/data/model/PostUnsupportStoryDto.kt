package com.ecosense.android.featForums.data.model

import com.google.gson.annotations.SerializedName

data class PostUnsupportStoryDto(
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
)