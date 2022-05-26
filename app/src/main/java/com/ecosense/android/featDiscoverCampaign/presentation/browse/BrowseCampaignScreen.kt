package com.ecosense.android.featDiscoverCampaign.presentation.browse

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
import com.ecosense.android.destinations.DetailCampaignScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun BrowseCampaignScreen(
    navigator: DestinationsNavigator,
    category: String?,
    viewModel: BrowseCampaignViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row {
                Text("Browse Campaign")
            }

            Row {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(viewModel.campaignList.value.size) { i ->
                        CampaignItem(
                            campaign = viewModel.campaignList.value[i],
                            category = category,
                            onClick = {
                                navigator.navigate(DetailCampaignScreenDestination(id = viewModel.campaignList.value[i].id))
                                Log.d("TAG", "BrowseCampaignScreen: clicked $i")
                            }
                        )
                    }
                }
            }
        }
    }
}