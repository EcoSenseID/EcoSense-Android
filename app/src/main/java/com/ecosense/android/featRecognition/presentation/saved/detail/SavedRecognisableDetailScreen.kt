package com.ecosense.android.featRecognition.presentation.saved.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
fun SavedRecognitionResultDetailScreen(
    navigator: DestinationsNavigator,
    savedRecognisableId: Int,
    viewModel: SavedRecognisableDetailViewModel = hiltViewModel(),
) {
    viewModel.setResultId(savedRecognisableId)
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) { scaffoldPadding ->
        Column(modifier = Modifier.padding(scaffoldPadding)) {
            Text(text = viewModel.state.value.toString())
        }
    }
}