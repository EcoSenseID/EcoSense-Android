package com.ecosense.android.featForums.data.model

data class GetStorySupportersDto(
    val error: Boolean?,
    val message: String?,
    val supporters: List<SupporterDto>?,
)