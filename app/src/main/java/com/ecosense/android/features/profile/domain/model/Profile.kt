package com.ecosense.android.features.profile.domain.model

data class Profile(
    val name: String,
    val occupation: String,
    val age: Int,
    val bio: String,
    val speciesList: List<Species>
)