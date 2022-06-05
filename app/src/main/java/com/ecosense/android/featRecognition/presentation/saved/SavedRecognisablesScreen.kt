package com.ecosense.android.featRecognition.presentation.saved

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.destinations.RecognisableDetailScreenDestination
import com.ecosense.android.featRecognition.presentation.model.toDetailParcelable
import com.ecosense.android.featRecognition.presentation.recognition.component.SavedRecognisablesTopBar
import com.ecosense.android.featRecognition.presentation.saved.component.SavedRecognisableItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
@Destination
fun SavedRecognitionResultsScreen(
    navigator: DestinationsNavigator,
    viewModel: SavedRecognisablesViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val scaffoldState = rememberScaffoldState()

    val state = viewModel.state.value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    focusManager.clearFocus()
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context)
                    )
                }

                is UIEvent.HideKeyboard -> {
                    focusManager.clearFocus()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            SavedRecognisablesTopBar(
                isLoading = state.isLoading,
                onBackClick = { navigator.navigateUp() }
            )
        },
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize()
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            AnimatedVisibility(visible = state.savedRecognisables.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(state.savedRecognisables.size) { i ->
                        if (i == 0) Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                        SavedRecognisableItem(state.savedRecognisables[i]) {
                            navigator.navigate(
                                RecognisableDetailScreenDestination(it.toDetailParcelable())
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(visible = state.savedRecognisables.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.medium)
                ) {
                    AsyncImage(
                        model = R.drawable.ic_wind_turbine_pana,
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    Text(
                        text = stringResource(R.string.empty_saved_recognition_results),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}