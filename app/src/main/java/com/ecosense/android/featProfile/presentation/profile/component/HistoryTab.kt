package com.ecosense.android.featProfile.presentation.profile.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ecosense.android.core.domain.model.Campaign
import com.ecosense.android.core.presentation.component.CampaignItem
import com.ecosense.android.core.presentation.theme.spacing
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import logcat.logcat

@Composable
fun HistoryTab(
    campaigns: List<Campaign>,
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(campaigns.size) { i ->
            if (i == 0) Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            CampaignItem(
                campaign = campaigns[i],
                onClick = {
                    // TODO: navigate to CampaignDetailScreen
                    logcat("HistoryTab") { "${campaigns[i].title} clicked" }
                }
            )
        }
    }
}