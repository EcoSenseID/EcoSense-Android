package com.ecosense.android.featReward.data.model

import com.google.gson.annotations.SerializedName

data class RequestRewardDto(
    @SerializedName("error") val error: Boolean?,
    @SerializedName("message") val message: String?
)