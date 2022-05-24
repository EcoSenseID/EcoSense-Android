package com.ecosense.android.featDiscoverCampaign.presentation.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun CategoryCampaignScreen(
    navigator: DestinationsNavigator,
    viewModel: CategoryCampaignViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row {
               Text("CategoryCampaign")
            }
        }
    }
}