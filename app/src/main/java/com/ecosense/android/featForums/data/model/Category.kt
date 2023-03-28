package com.ecosense.android.featForums.data.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("colorHex") val colorHex: String,
    @SerializedName("name") val name: String
)