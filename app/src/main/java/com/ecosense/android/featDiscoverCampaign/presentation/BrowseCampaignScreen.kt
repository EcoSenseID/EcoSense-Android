package com.ecosense.android.featDiscoverCampaign.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
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
                LazyColumn {
                    items(viewModel.campaignList.value.size) { i ->
                        val campaign = viewModel.campaignList.value[i]
                        Row(
                            modifier = Modifier.clickable {
                                Log.d("TAG malas", "BrowseCampaignScreen: clicked ${campaign.title}")
                            }
                        ) {
                            Column(
                                modifier = Modifier.background(MaterialTheme.colors.primary)
                            ) {
                                if (campaign.isNew) Text(
                                    text = "New",
                                    color = MaterialTheme.colors.onPrimary
                                )
                                if (campaign.isTrending) Text(
                                    text = "Trending",
                                    color = MaterialTheme.colors.onPrimary
                                )
                                Icon(
                                    imageVector = Icons.Default.Campaign,
                                    contentDescription = campaign.title,
                                    tint = MaterialTheme.colors.onPrimary
                                )
                            }
                            Column {
                                Text(
                                    text = campaign.endDate,
                                    style = MaterialTheme.typography.caption
                                )
                                Text(
                                    text = campaign.title,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(text = campaign.category)

                                Text(text = campaign.participantsCount.toString())
                            }
                        }
                    }
                }
            }
        }
    }
}