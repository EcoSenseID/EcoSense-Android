package com.ecosense.android.featProfile.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun HistoryTab(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {
    // TODO: create history tab
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "History here")
    }
}