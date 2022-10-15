package com.ecosense.android.featProfile.presentation.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun SettingsScreen(
    navigator: DestinationsNavigator,
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
    ) { scaffoldPadding ->
        LazyColumn(modifier = Modifier.padding(scaffoldPadding)) {
            item { Text(text = "SettingsScreen") }
        }
    }
}