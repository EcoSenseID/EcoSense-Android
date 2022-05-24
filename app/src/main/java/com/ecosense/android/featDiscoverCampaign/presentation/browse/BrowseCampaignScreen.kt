package com.ecosense.android.featDiscoverCampaign.presentation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.core.presentation.component.CampaignItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun BrowseCampaignScreen(
    navigator: DestinationsNavigator,
    viewModel: BrowseCampaignViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row {
                Text("Browser Campaign")
            }

            Row {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(viewModel.campaignList.value.size) { i ->
                        CampaignItem(
                            campaign = viewModel.campaignList.value[i],
                            onClick = {
                                // TODO: navigate to CampaignDetailScreen
                                Log.d("TAG", "BrowseCampaignScreen: clicked $i")
                            }
                        )
                    }
                }
            }
        }
    }
}