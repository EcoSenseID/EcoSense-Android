package com.ecosense.android.featRecognition.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ecosense.android.R
import com.ecosense.android.core.presentation.theme.spacing
import com.ecosense.android.core.presentation.util.UIEvent
import com.ecosense.android.core.presentation.util.asString
import com.ecosense.android.core.util.Constants
import com.ecosense.android.featRecognition.domain.model.SavedRecognitionResult
import com.ecosense.android.featRecognition.presentation.component.SavedRecognitionTopBar
import com.ecosense.android.featRecognition.presentation.saved.SavedRecognitionViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.*

@Composable
@Destination
fun RecognitionHistoryScreen(
    navigator: DestinationsNavigator,
    viewModel: SavedRecognitionViewModel = hiltViewModel()
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
            SavedRecognitionTopBar(
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
            AnimatedVisibility(visible = state.resultList.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(state.resultList.size) { i ->
                        if (i == 0) Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                        SavedRecognitionItem(state.resultList[i])
                    }
                }
            }

            AnimatedVisibility(visible = state.resultList.isEmpty()) {
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

@Composable
fun SavedRecognitionItem(
    savedRecognitionResult: SavedRecognitionResult,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(
                start = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.medium,
                bottom = MaterialTheme.spacing.medium
            )
            .background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(MaterialTheme.spacing.medium)
            .fillMaxWidth()
    ) {
        val sdf = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
        val date = Date().apply { this.time = savedRecognitionResult.timeInMillis }
        Text(
            text = sdf.format(date),
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Text(
            text = savedRecognitionResult.label,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Text(
            text = "Confidence: ${savedRecognitionResult.confidencePercent}%",
            color = MaterialTheme.colors.onSurface
        )
    }
}
