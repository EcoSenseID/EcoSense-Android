package com.ecosense.android.featProfile.presentation.profile.model

import androidx.compose.runtime.Composable

data class TabItem(
    val title: String,
    val content: @Composable () -> Unit
)