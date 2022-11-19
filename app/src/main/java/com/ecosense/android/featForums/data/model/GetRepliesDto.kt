package com.ecosense.android.featForums.data.model

import com.google.gson.annotations.SerializedName

data class GetRepliesDto(
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("replies") val replies: List<ReplyDto>?,
)