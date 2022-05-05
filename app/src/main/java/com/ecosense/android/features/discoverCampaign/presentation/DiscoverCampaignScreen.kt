package com.ecosense.android.features.discoverCampaign.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ecosense.android.R
import com.ecosense.android.features.destinations.DiseaseRecognitionScreenDestination
import com.ecosense.android.features.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination(start = true)
fun DiscoverCampaignScreen(
    navigator: DestinationsNavigator,
    viewModel: DiscoverCampaignViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text("DiscoverCampaign Screen")

            Button(onClick = { navigator.navigate(DiseaseRecognitionScreenDestination) }) {
                Text(stringResource(R.string.plant_disease_recognition))
            }

            Button(onClick = { navigator.navigate(ProfileScreenDestination) }) {
                Text(stringResource(R.string.profile))
            }
        }
    }
}