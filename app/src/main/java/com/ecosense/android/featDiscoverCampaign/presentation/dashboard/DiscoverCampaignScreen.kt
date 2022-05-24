package com.ecosense.android.featDiscoverCampaign.presentation.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.destinations.BrowseCampaignScreenDestination
import com.ecosense.android.destinations.CategoryCampaignScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
@RootNavGraph(start = true)
fun DiscoverCampaignScreen(
    navigator: DestinationsNavigator,
    viewModel: DiscoverCampaignViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row {
                Text("DiscoverCampaign")
            }
            Row {
                Button(onClick = {
                    navigator.navigate(BrowseCampaignScreenDestination)
                }) {
                    Text("View all")
                }
            }
            Row {
                Button(onClick = {
                    navigator.navigate(CategoryCampaignScreenDestination)
                }) {
                    Text("View Category")
                }
            }
        }
    }
}